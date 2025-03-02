package com.exatask.platform.storage.transports;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.exatask.platform.storage.constants.MetadataProperties;
import com.exatask.platform.storage.exceptions.CopyFailedException;
import com.exatask.platform.storage.exceptions.DeleteFailedException;
import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.storage.exceptions.UploadFailedException;
import com.exatask.platform.storage.upload.CopyResponse;
import com.exatask.platform.storage.upload.MoveResponse;
import com.exatask.platform.storage.upload.UploadResponse;
import com.exatask.platform.utilities.constants.GcpConstant;
import com.exatask.platform.utilities.properties.GcpProperties;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class GcpTransport extends AppTransport {

  private final Storage storageClient;

  private final Map<String, GcpProperties.StorageProperties> bucketProperties;

  public GcpTransport(GcpProperties gcpProperties) throws IOException {

    storageClient = StorageOptions.newBuilder()
        .setCredentials(gcpProperties.getCredentialsProvider())
        .build()
        .getService();

    bucketProperties = gcpProperties.getStorage().stream()
        .collect(Collectors.toMap(GcpProperties.StorageProperties::getBucketKey, Function.identity()));
  }

  @Override
  public Health health() {

    Health.Builder entityHealth = Health.up()
        .withDetail("type", AppTransportType.GCP);

    Page<Bucket> bucketsResponse = storageClient.list();
    List<String> bucketNames = new ArrayList<>();
    bucketsResponse.getValues()
        .forEach(bucket -> bucketNames.add(bucket.getName()));
    entityHealth.withDetail("buckets", bucketNames);

    return entityHealth.build();
  }

  @Override
  public UploadResponse upload(Path inputPath, String uploadPath, Map<MetadataProperties, String> metadata, Map<String, String> tags) {

    GcpProperties.StorageProperties bucketProperty = getBucket(uploadPath);
    String uploadKey = uploadPath.replace(bucketProperty.getBucketName() + FILE_SEPARATOR, "");

    Acl objectAcl = Acl.of(Acl.User.ofAllUsers(), Acl.Role.valueOf(bucketProperty.getAcl().toString()));

    BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(bucketProperty.getBucketName(), uploadKey);
    blobInfoBuilder.setAcl(Collections.singletonList(objectAcl))
        .setStorageClass(StorageClass.valueOf(bucketProperty.getStorageClass().toString()))
        .setMetadata(prepareMetadata(blobInfoBuilder, metadata, tags));

    try {
      blobInfoBuilder.setContentType(Files.probeContentType(inputPath));
    } catch (IOException exception) {
      LOGGER.error(exception);
    }

    try {
      storageClient.create(BlobInfo.newBuilder(bucketProperty.getBucketName(), uploadKey).build(),
          Files.readAllBytes(inputPath));
    } catch (IOException exception) {
      LOGGER.error(exception.toString());
      throw new UploadFailedException(uploadPath, null);
    }

    return UploadResponse.builder()
        .fileUrl(this.getPublicUrl(bucketProperty, uploadKey, null))
        .fileUri(AppTransportType.GCP.getPathPrefix() + uploadPath)
        .build();
  }

  @Override
  public Path download(String downloadPath) {

    downloadPath = downloadPath.replace(AppTransportType.GCP.getPathPrefix(), "");
    GcpProperties.StorageProperties bucketProperty = getBucket(downloadPath);
    String downloadKey = downloadPath.replace(bucketProperty.getBucketName() + FILE_SEPARATOR, "");

    try {

      Blob response = storageClient.get(bucketProperty.getBucketName(), downloadKey);
      Path outputFile = createTempFile(AppTransportType.GCP);
      if (response != null) {
        response.downloadTo(outputFile);
      }
      return outputFile;

    } catch (IOException exception) {

      LOGGER.error(exception);
      throw new DownloadFailedException(downloadPath, exception);
    }
  }

  @Override
  public MoveResponse move(String sourcePath, String destinationPath) {

    CopyResponse copyResponse = copy(sourcePath, destinationPath);
    delete(sourcePath);

    return MoveResponse.builder()
        .fileUrl(copyResponse.getFileUrl())
        .fileUri(copyResponse.getFileUri())
        .build();
  }

  @Override
  public CopyResponse copy(String sourcePath, String destinationPath) {

    GcpProperties.StorageProperties destinationBucket = getBucket(destinationPath);
    String destinationKey = destinationPath.replace(destinationBucket.getBucketName() + FILE_SEPARATOR, "");

    sourcePath = sourcePath.replace(AppTransportType.GCP.getPathPrefix(), "");
    GcpProperties.StorageProperties sourceBucket = getBucket(sourcePath);
    String sourceKey = sourcePath.replace(sourceBucket.getBucketName() + FILE_SEPARATOR, "");

    Acl objectAcl = Acl.of(Acl.User.ofAllUsers(), Acl.Role.valueOf(destinationBucket.getAcl().toString()));
    BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(destinationBucket.getBucketName(), destinationKey);
    blobInfoBuilder.setAcl(Collections.singletonList(objectAcl))
        .setStorageClass(StorageClass.valueOf(destinationBucket.getStorageClass().toString()));

    Storage.CopyRequest copyRequest = Storage.CopyRequest.newBuilder()
        .setSource(sourceBucket.getBucketName(), sourceKey)
        .setTarget(BlobInfo.newBuilder(destinationBucket.getBucketName(), destinationKey).build())
        .build();

    try {

      CopyWriter copyWriter = storageClient.copy(copyRequest);
      if (!copyWriter.isDone()) {
        throw new CopyFailedException(sourcePath, destinationPath, null);
      }

    } catch (StorageException exception) {
      LOGGER.error(exception);
      throw new CopyFailedException(sourcePath, destinationPath, exception);
    }

    return CopyResponse.builder()
        .fileUrl(this.getPublicUrl(destinationBucket, destinationKey, null))
        .fileUri(AppTransportType.GCP.getPathPrefix() + destinationPath)
        .build();
  }

  @Override
  public boolean delete(String filePath) {

    filePath = filePath.replace(AppTransportType.GCP.getPathPrefix(), "");
    GcpProperties.StorageProperties storageProperties = getBucket(filePath);
    String fileKey = filePath.replace(storageProperties.getBucketName() + FILE_SEPARATOR, "");

    try {
      boolean deleted = storageClient.delete(storageProperties.getBucketName(), fileKey);
      if (!deleted) {
        throw new DeleteFailedException(filePath, null);
      }
    } catch (StorageException exception) {
      LOGGER.error(exception);
      throw new DeleteFailedException(filePath, exception);
    }

    return true;
  }

  @Override
  public String url(String filePath, Long ttl) {

    filePath = filePath.replace(AppTransportType.GCP.getPathPrefix(), "");
    GcpProperties.StorageProperties storageProperties = getBucket(filePath);
    String fileKey = filePath.replace(storageProperties.getBucketName() + FILE_SEPARATOR, "");

    return this.getPublicUrl(storageProperties, fileKey, ttl);
  }

  private GcpProperties.StorageProperties getBucket(String filePath) {

    String bucketKey = Arrays.stream(filePath.split(FILE_SEPARATOR)).findFirst().orElse("");
    if (StringUtils.isEmpty(bucketKey)) {
      LOGGER.error(String.format("Bucket not found for file path: %s", filePath));
      throw new UploadFailedException(filePath, null);
    }

    return bucketProperties.get(bucketKey);
  }

  private Map<String, String> prepareMetadata(BlobInfo.Builder blobInfoBuilder, Map<MetadataProperties, String> metadata, Map<String, String> tags) {

    if (metadata.containsKey(MetadataProperties.DOWNLOAD_FILENAME)) {
      blobInfoBuilder.setContentDisposition(String.format("attachment; filename=\"%s\"", metadata.get(MetadataProperties.DOWNLOAD_FILENAME)));
    }

    Map<String, String> objectMetadata = new HashMap<>();

    if (!CollectionUtils.isEmpty(metadata)) {
      objectMetadata.putAll(metadata.entrySet()
          .stream()
          .collect(Collectors.toMap(property -> property.getKey().getGcpKey(), Map.Entry::getValue)));
    }

    if (!CollectionUtils.isEmpty(tags)) {
      objectMetadata.putAll(tags);
    }

    return objectMetadata;
  }

  private String getPublicUrl(GcpProperties.StorageProperties bucketProperties, String objectKey, Long ttl) {

    if (bucketProperties.getAcl() == GcpConstant.StorageAcl.READER) {
      return String.format("https://storage.googleapis.com/%s/%s", bucketProperties.getBucketName(), objectKey);
    } else {

      BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(bucketProperties.getBucketName(), objectKey);
      URL url = storageClient.signUrl(blobInfoBuilder.build(), Optional.ofNullable(ttl).orElse(60L), TimeUnit.SECONDS);
      return url.toString();
    }
  }
}
