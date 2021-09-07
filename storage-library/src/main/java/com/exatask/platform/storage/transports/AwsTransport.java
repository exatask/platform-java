package com.exatask.platform.storage.transports;

import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.storage.exceptions.InvalidPathException;
import com.exatask.platform.utilities.properties.AwsProperties;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class AwsTransport extends AppTransport {

  private final S3Client s3Client;

  private final Map<String, AwsProperties.S3Properties> buckets;

  public AwsTransport(AwsProperties awsProperties) {

    s3Client = S3Client.builder()
        .region(awsProperties.getRegion())
        .credentialsProvider(awsProperties.getCredentialsProvider())
        .build();

    buckets = awsProperties.getS3();
  }

  @Override
  public String upload(Path inputPath, String uploadPath, Map<String, String> properties) {

    String[] uploadPathParts = uploadPath.split(File.separator, 2);
    if (!buckets.containsKey(uploadPathParts[0])) {
      throw new InvalidPathException(uploadPath);
    }

    AwsProperties.S3Properties bucketProperties = buckets.get(uploadPathParts[0]);
    PutObjectRequest.Builder putObjectRequestBuilder = PutObjectRequest.builder()
        .bucket(bucketProperties.getBucket())
        .key(uploadPathParts[1])
        .acl(bucketProperties.getAcl())
        .storageClass(bucketProperties.getStorageClass());

    if (properties.containsKey(DOWNLOAD_NAME_KEY)) {
      putObjectRequestBuilder.contentDisposition(String.format("attachment; filename=\"%s\"", properties.get(DOWNLOAD_NAME_KEY)));
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

    String[] downloadPathParts = downloadPath.split(File.separator, 2);
    if (!buckets.containsKey(downloadPathParts[0])) {
      throw new InvalidPathException(downloadPath);
    }

    AwsProperties.S3Properties bucketProperties = buckets.get(downloadPathParts[0]);
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketProperties.getBucket())
        .key(downloadPathParts[1])
        .build();

    ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);

    try {

      Path outputFile = Files.createTempFile(AppTransportType.AWS.getPathPrefix(), "");
      Files.copy(new BufferedInputStream(response), outputFile);
      return outputFile;

    } catch (IOException exception) {

      LOGGER.error(exception);
      throw new DownloadFailedException(downloadPath, exception);
    }
  }

  @Override
  public String copy(String sourcePath, String destinationPath, Map<String, String> properties) {

    String[] sourcePathParts = sourcePath.split(File.separator, 2);
    String[] destinationPathParts = destinationPath.split(File.separator, 2);

    if (!buckets.containsKey(sourcePathParts[0])) {
      throw new InvalidPathException(sourcePath);
    } else if (!buckets.containsKey(destinationPathParts[0])) {
      throw new InvalidPathException((destinationPath));
    }

    AwsProperties.S3Properties sourceBucketProperties = buckets.get(sourcePathParts[0]);
    AwsProperties.S3Properties destinationBucketProperties = buckets.get(destinationPathParts[0]);

    CopyObjectRequest.Builder copyObjectRequestBuilder = CopyObjectRequest.builder()
        .copySource(sourceBucketProperties.getBucket() + "/" + sourcePathParts[1])
        .destinationBucket(destinationBucketProperties.getBucket())
        .destinationKey(destinationPathParts[1])
        .acl(destinationBucketProperties.getAcl())
        .storageClass(destinationBucketProperties.getStorageClass());

    if (properties.containsKey(DOWNLOAD_NAME_KEY)) {
      copyObjectRequestBuilder.contentDisposition(String.format("attachment; filename=\"%s\"", properties.get(DOWNLOAD_NAME_KEY)));
    }

    s3Client.copyObject(copyObjectRequestBuilder.build());
    return AppTransportType.AWS.getPathPrefix() + destinationPath;
  }
}
