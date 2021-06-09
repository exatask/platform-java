package com.exatask.platform.utilities.properties;

import com.exatask.platform.utilities.ServiceUtility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwsProperties {

  private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
  private static final String AWS_SECRET_KEY = "aws.secretKey";
  private static final String AWS_REGION = "aws.region";

  private static final Region DEFAULT_REGION = Region.AP_SOUTH_1;

  private String region;

  private String accessKeyId;

  private String secretKey;

  public Region getRegion() {

    if (StringUtils.isEmpty(this.region)) {
      region = ServiceUtility.getServiceProperty(AWS_REGION);
    }

    if (StringUtils.isNotEmpty(region)) {
      return ObjectUtils.defaultIfNull(Region.of(region), DEFAULT_REGION);
    } else {
      return DEFAULT_REGION;
    }
  }

  public AwsCredentialsProvider getCredentialsProvider() {

    if (StringUtils.isEmpty(accessKeyId)) {
      accessKeyId = ServiceUtility.getServiceProperty(AWS_ACCESS_KEY_ID);
    }

    if (StringUtils.isEmpty(secretKey)) {
      secretKey = ServiceUtility.getServiceProperty(AWS_SECRET_KEY);
    }

    if (StringUtils.isNotEmpty(accessKeyId) && StringUtils.isNotEmpty(secretKey)) {

      AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKeyId, secretKey);
      return StaticCredentialsProvider.create(basicCredentials);

    } else {
      return DefaultCredentialsProvider.create();
    }
  }
}
