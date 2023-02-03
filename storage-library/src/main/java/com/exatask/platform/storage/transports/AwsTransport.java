package com.exatask.platform.storage.transports;

import com.exatask.platform.storage.constants.MetadataProperties;
import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.storage.exceptions.UploadFailedException;
import com.exatask.platform.utilities.properties.AwsProperties;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.StorageClass;
import software.amazon.awssdk.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
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
  public String upload(Path inputPath, String uploadPath, Map<MetadataProperties, String> properties) {

    AwsProperties.S3Properties bucketProperty = getBucket(uploadPath);

    PutObjectRequest.Builder putObjectRequestBuilder = PutObjectRequest.builder()
        .bucket(bucketProperty.getBucket())
        .key(uploadPath.replace(bucketProperty.getBucket() + FILE_SEPARATOR, ""))
        .acl(ObjectCannedACL.valueOf(bucketProperty.getAcl().toString()))
        .storageClass(StorageClass.valueOf(bucketProperty.getStorageClass().toString()))
        .metadata(prepareMetadata(properties));

    if (properties.containsKey(MetadataProperties.DOWNLOAD_FILENAME)) {
      putObjectRequestBuilder.contentDisposition(String.format("attachment; filename=\"%s\"", properties.get(MetadataProperties.DOWNLOAD_FILENAME)));
    }

    try {
      putObjectRequestBuilder.contentType(Files.probeContentType(inputPath));
    } catch (IOException exception) {
      LOGGER.error(exception);
    }

    s3Client.putObject(putObjectRequestBuilder.build(), inputPath);
    return AppTransportType.AWS.getPathPrefix() + uploadPath;
  }

  @Override
  public Path download(String downloadPath) {

    AwsProperties.S3Properties bucketProperty = getBucket(downloadPath);

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketProperty.getBucket())
        .key(downloadPath.replace(AppTransportType.AWS.getPathPrefix() + bucketProperty.getBucket() + FILE_SEPARATOR, ""))
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
  public String copy(String sourcePath, String destinationPath, Map<MetadataProperties, String> properties) {

    AwsProperties.S3Properties bucketProperty = getBucket(destinationPath);

    CopyObjectRequest.Builder copyObjectRequestBuilder = CopyObjectRequest.builder()
        .copySource(sourcePath)
        .destinationBucket(bucketProperty.getBucket())
        .destinationKey(destinationPath.replace(bucketProperty.getBucket() + FILE_SEPARATOR, ""))
        .acl(ObjectCannedACL.valueOf(bucketProperty.getAcl().toString()))
        .storageClass(StorageClass.valueOf(bucketProperty.getStorageClass().toString()));

    if (properties.containsKey(MetadataProperties.DOWNLOAD_FILENAME)) {
      copyObjectRequestBuilder.contentDisposition(String.format("attachment; filename=\"%s\"", properties.get(MetadataProperties.DOWNLOAD_FILENAME)));
    }

    s3Client.copyObject(copyObjectRequestBuilder.build());
    return AppTransportType.AWS.getPathPrefix() + destinationPath;
  }

  private AwsProperties.S3Properties getBucket(String filePath) {

    String bucket = Arrays.stream(filePath.split(FILE_SEPARATOR)).findFirst().orElse("");
    if (StringUtils.isEmpty(bucket)) {
      throw new UploadFailedException(filePath, null);
    }

    return bucketProperties.get(bucket);
  }
}
