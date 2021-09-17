package com.exatask.platform.storage.transports;

import com.exatask.platform.storage.constants.UploadProperties;
import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.utilities.properties.AwsProperties;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.StorageClass;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class AwsTransport extends AppTransport {

  private final S3Client s3Client;

  private final AwsProperties.S3Properties bucketProperties;

  public AwsTransport(AwsProperties awsProperties) {

    s3Client = S3Client.builder()
        .region(awsProperties.getRegion())
        .credentialsProvider(awsProperties.getCredentialsProvider())
        .build();

    bucketProperties = awsProperties.getS3();
  }

  @Override
  public String upload(Path inputPath, String uploadPath, Map<UploadProperties, String> properties) {

    PutObjectRequest.Builder putObjectRequestBuilder = PutObjectRequest.builder()
        .bucket(bucketProperties.getBucket())
        .key(uploadPath)
        .acl(ObjectCannedACL.fromValue(bucketProperties.getAcl().toString()))
        .storageClass(StorageClass.fromValue(bucketProperties.getStorageClass().toString()))
        .metadata(prepareMetadata(inputPath));

    if (properties.containsKey(UploadProperties.DOWNLOAD_NAME)) {
      putObjectRequestBuilder.contentDisposition(String.format("attachment; filename=\"%s\"", properties.get(UploadProperties.DOWNLOAD_NAME)));
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

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketProperties.getBucket())
        .key(downloadPath)
        .build();

    ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);

    try {

      AppTransportType transportType = AppTransportType.AWS;
      Path outputFile = Files.createTempFile(transportType.getFilePrefix(), transportType.getFileSuffix());
      Files.copy(new BufferedInputStream(response), outputFile);
      return outputFile;

    } catch (IOException exception) {

      LOGGER.error(exception);
      throw new DownloadFailedException(downloadPath, exception);
    }
  }

  @Override
  public String copy(String sourcePath, String destinationPath, Map<UploadProperties, String> properties) {

    CopyObjectRequest.Builder copyObjectRequestBuilder = CopyObjectRequest.builder()
        .copySource(sourcePath)
        .destinationBucket(bucketProperties.getBucket())
        .destinationKey(destinationPath)
        .acl(ObjectCannedACL.fromValue(bucketProperties.getAcl().toString()))
        .storageClass(StorageClass.fromValue(bucketProperties.getStorageClass().toString()));

    if (properties.containsKey(UploadProperties.DOWNLOAD_NAME)) {
      copyObjectRequestBuilder.contentDisposition(String.format("attachment; filename=\"%s\"", properties.get(UploadProperties.DOWNLOAD_NAME)));
    }

    s3Client.copyObject(copyObjectRequestBuilder.build());
    return AppTransportType.AWS.getPathPrefix() + destinationPath;
  }
}
