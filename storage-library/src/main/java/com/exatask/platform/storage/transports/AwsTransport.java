package com.exatask.platform.storage.transports;

import com.exatask.platform.storage.constants.MetadataProperties;
import com.exatask.platform.storage.exceptions.CopyFailedException;
import com.exatask.platform.storage.exceptions.DeleteFailedException;
import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.storage.exceptions.UploadFailedException;
import com.exatask.platform.storage.upload.CopyResponse;
import com.exatask.platform.storage.upload.MoveResponse;
import com.exatask.platform.storage.upload.UploadResponse;
import com.exatask.platform.utilities.constants.AwsConstant;
import com.exatask.platform.utilities.properties.AwsProperties;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.StorageClass;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AwsTransport extends AppTransport {

  private final S3Client s3Client;

  private final Map<String, AwsProperties.S3Properties> bucketProperties;

  public AwsTransport(AwsProperties awsProperties) {

    s3Client = S3Client.builder()
        .region(awsProperties.getRegion())
        .credentialsProvider(awsProperties.getCredentialsProvider())
        .build();

    bucketProperties = awsProperties.getS3().stream()
        .collect(Collectors.toMap(AwsProperties.S3Properties::getBucket, Function.identity()));
  }

  @Override
  public UploadResponse upload(Path inputPath, String uploadPath, Map<MetadataProperties, String> properties) {

    AwsProperties.S3Properties bucketProperty = getBucket(uploadPath);
    String uploadKey = uploadPath.replace(bucketProperty.getBucket() + FILE_SEPARATOR, "");

    PutObjectRequest.Builder putObjectRequestBuilder = PutObjectRequest.builder()
        .bucket(bucketProperty.getBucket())
        .key(uploadKey)
        .acl(ObjectCannedACL.valueOf(bucketProperty.getAcl().toString()))
        .storageClass(StorageClass.valueOf(bucketProperty.getStorageClass().toString()))
        .metadata(prepareMetadata(properties));

    prepareObjectProperties(putObjectRequestBuilder, properties);
    try {
      putObjectRequestBuilder.contentType(Files.probeContentType(inputPath));
    } catch (IOException exception) {
      LOGGER.error(exception);
    }

    PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequestBuilder.build(), inputPath);
    if (!putObjectResponse.sdkHttpResponse().isSuccessful()) {
      LOGGER.error(putObjectResponse.responseMetadata().toString());
      throw new UploadFailedException(uploadPath, null);
    }

    return UploadResponse.builder()
            .fileUrl(this.getPublicUrl(bucketProperty, uploadKey, null))
            .fileUri(AppTransportType.AWS.getPathPrefix() + uploadPath)
            .build();
  }

  @Override
  public Path download(String downloadPath) {

    downloadPath = downloadPath.replace(AppTransportType.AWS.getPathPrefix(), "");
    AwsProperties.S3Properties bucketProperty = getBucket(downloadPath);
    String downloadKey = downloadPath.replace(bucketProperty.getBucket() + FILE_SEPARATOR, "");

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketProperty.getBucket())
        .key(downloadKey)
        .build();

    ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);

    try {

      Path outputFile = createTempFile(AppTransportType.AWS);
      Files.copy(new BufferedInputStream(response), outputFile);
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

    AwsProperties.S3Properties destinationBucket = getBucket(destinationPath);
    String destinationKey = destinationPath.replace(destinationBucket.getBucket() + FILE_SEPARATOR, "");

    sourcePath = sourcePath.replace(AppTransportType.AWS.getPathPrefix(), "");
    AwsProperties.S3Properties sourceBucket = getBucket(sourcePath);
    String sourceKey = sourcePath.replace(sourceBucket.getBucket() + FILE_SEPARATOR, "");

    CopyObjectRequest.Builder copyObjectRequestBuilder = CopyObjectRequest.builder()
            .sourceBucket(sourceBucket.getBucket())
            .sourceKey(sourceKey)
            .destinationBucket(destinationBucket.getBucket())
            .destinationKey(destinationKey)
            .acl(ObjectCannedACL.valueOf(destinationBucket.getAcl().toString()))
            .storageClass(StorageClass.valueOf(destinationBucket.getStorageClass().toString()));

    CopyObjectResponse copyObjectResponse = s3Client.copyObject(copyObjectRequestBuilder.build());
    if (!copyObjectResponse.sdkHttpResponse().isSuccessful()) {
      LOGGER.error(copyObjectResponse.responseMetadata().toString());
      throw new CopyFailedException(sourcePath, destinationPath, null);
    }

    return CopyResponse.builder()
            .fileUrl(this.getPublicUrl(destinationBucket, destinationKey, null))
            .fileUri(AppTransportType.AWS.getPathPrefix() + destinationPath)
            .build();
  }

  @Override
  public boolean delete(String filePath) {

    filePath = filePath.replace(AppTransportType.AWS.getPathPrefix(), "");
    AwsProperties.S3Properties fileBucket = getBucket(filePath);
    String fileKey = filePath.replace(fileBucket.getBucket() + FILE_SEPARATOR, "");

    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(fileBucket.getBucket())
            .key(fileKey)
            .build();

    DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);
    if (!deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
      LOGGER.error(deleteObjectResponse.responseMetadata().toString());
      throw new DeleteFailedException(filePath, null);
    }

    return true;
  }

  @Override
  public String url(String filePath, Long ttl) {

    filePath = filePath.replace(AppTransportType.AWS.getPathPrefix(), "");
    AwsProperties.S3Properties fileBucket = getBucket(filePath);
    String fileKey = filePath.replace(fileBucket.getBucket() + FILE_SEPARATOR, "");

    return this.getPublicUrl(fileBucket, fileKey, ttl);
  }

  private AwsProperties.S3Properties getBucket(String filePath) {

    String bucket = Arrays.stream(filePath.split(FILE_SEPARATOR)).findFirst().orElse("");
    if (StringUtils.isEmpty(bucket)) {
      LOGGER.error(String.format("Bucket not found in file path: %s", filePath));
      throw new UploadFailedException(filePath, null);
    }

    return bucketProperties.get(bucket);
  }

  private String getPublicUrl(AwsProperties.S3Properties bucketProperties, String objectKey, Long ttl) {

    if (bucketProperties.getAcl() == AwsConstant.S3Acl.PUBLIC_READ) {

      GetUrlRequest getUrlRequest = GetUrlRequest.builder()
              .bucket(bucketProperties.getBucket())
              .key(objectKey)
              .build();
      URL s3FileUrl = s3Client.utilities().getUrl(getUrlRequest);
      return s3FileUrl.toString();

    } else {

      GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
              .getObjectRequest(getObjectRequest -> getObjectRequest.bucket(bucketProperties.getBucket())
                      .key(objectKey))
              .signatureDuration(Duration.of(Optional.ofNullable(ttl).orElse(60L), ChronoUnit.SECONDS))
              .build();

      PresignedGetObjectRequest presignedGetObjectRequest = S3Presigner.create()
              .presignGetObject(getObjectPresignRequest);
      return presignedGetObjectRequest.url().toString();
    }
  }

  private void prepareObjectProperties(PutObjectRequest.Builder putObjectRequestBuilder, Map<MetadataProperties, String> properties) {

    if (properties.containsKey(MetadataProperties.DOWNLOAD_FILENAME)) {
      putObjectRequestBuilder.contentDisposition(String.format("attachment; filename=\"%s\"", properties.get(MetadataProperties.DOWNLOAD_FILENAME)));
    }
  }
}
