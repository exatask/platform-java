package com.exatask.platform.utilities.cloud.aws;

import com.exatask.platform.utilities.ApplicationUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public class AwsCredentialUtility {

  private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
  private static final String AWS_SECRET_KEY = "aws.secretKey";
  private static final String AWS_REGION = "aws.region";

  private static final Region DEFAULT_REGION = Region.AP_SOUTH_1;

  public static Region getRegion() {

    String region = ApplicationUtility.getApplicationProperty(AWS_REGION);
    if (StringUtils.isNotEmpty(region)) {
      return ObjectUtils.defaultIfNull(Region.of(region), DEFAULT_REGION);
    } else {
      return DEFAULT_REGION;
    }
  }

  public static AwsCredentialsProvider getCredentialsProvider() {

    String accessKeyId = ApplicationUtility.getApplicationProperty(AWS_ACCESS_KEY_ID);
    String secretKey = ApplicationUtility.getApplicationProperty(AWS_SECRET_KEY);

    if (StringUtils.isNotEmpty(accessKeyId) && StringUtils.isNotEmpty(secretKey)) {

      AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKeyId, secretKey);
      return StaticCredentialsProvider.create(basicCredentials);

    } else {
      return DefaultCredentialsProvider.create();
    }
  }
}
