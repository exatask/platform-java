package com.exatask.platform.utilities.unit.properties;

import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.properties.AwsProperties;
import org.instancio.Gen;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;

import java.util.stream.Stream;

public class AwsPropertiesTest {

  private static MockedStatic<ServiceUtility> serviceUtility;

  @BeforeAll
  public static void setUp() {
    serviceUtility = Mockito.mockStatic(ServiceUtility.class);
  }

  @AfterAll
  public static void tearDown() {
    serviceUtility.close();
  }

  @ParameterizedTest
  @MethodSource("shouldReturnRegionParameters")
  public void shouldReturnRegion_getRegion(AwsProperties awsProperties, String region, Region result) {

    serviceUtility.clearInvocations();
    serviceUtility.when(() -> ServiceUtility.getServiceProperty("aws.region", "ap-south-1"))
        .thenReturn(region);

    Assertions.assertEquals(result, awsProperties.getRegion());
  }

  public static Stream<Arguments> shouldReturnRegionParameters() {

    AwsProperties awsProperties = Instancio.of(AwsProperties.class)
        .set(Select.field(AwsProperties.class, "region"), "us-west-1")
        .create();

    return Stream.of(
        Arguments.of(new AwsProperties(), "ap-south-2", Region.AP_SOUTH_2),
        Arguments.of(new AwsProperties(), "ap-south-1", Region.AP_SOUTH_1),
        Arguments.of(awsProperties, "us-west-1", Region.US_WEST_1)
    );
  }

  @ParameterizedTest
  @MethodSource("shouldReturnCredentialsProviderParameters")
  public void shouldReturnCredentialsProvider_getCredentialsProvider(String accessKeyId, String secretKey, String awsAccessKeyId, String awsSecretKey, AwsBasicCredentials result) {

    AwsProperties awsProperties = Instancio.of(AwsProperties.class)
        .set(Select.field(AwsProperties.class, "accessKeyId"), awsAccessKeyId)
        .set(Select.field(AwsProperties.class, "secretKey"), awsSecretKey)
        .create();

    serviceUtility.when(() -> ServiceUtility.getServiceProperty("aws.accessKeyId"))
        .thenReturn(accessKeyId);
    serviceUtility.when(() -> ServiceUtility.getServiceProperty("aws.secretKey"))
        .thenReturn(secretKey);

    AwsCredentials credentials = awsProperties.getCredentialsProvider().resolveCredentials();
    Assertions.assertEquals(result.accessKeyId(), credentials.accessKeyId());
    Assertions.assertEquals(result.secretAccessKey(), credentials.secretAccessKey());
  }

  public static Stream<Arguments> shouldReturnCredentialsProviderParameters() {

    String accessKeyId = Gen.string().get();
    String secretKey = Gen.string().get();

    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretKey);

    return Stream.of(
        Arguments.of(accessKeyId, secretKey, null, null, awsBasicCredentials),
        Arguments.of(accessKeyId, secretKey, accessKeyId, null, awsBasicCredentials),
        Arguments.of(accessKeyId, secretKey, null, secretKey, awsBasicCredentials),
        Arguments.of(accessKeyId, secretKey, accessKeyId, secretKey, awsBasicCredentials)
    );
  }
}
