package com.exatask.platform.utilities.unit;

import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;
import org.instancio.Gen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class ServiceUtilityTest {

  @Mock
  private Environment environment;

  @InjectMocks
  private ServiceUtility serviceUtility;

  @Test
  public void shouldThrowException_getServiceName() {

    Mockito.when(environment.getProperty("spring.application.name")).thenReturn(null);

    RuntimePropertyNotFoundException exception = Assertions.assertThrows(RuntimePropertyNotFoundException.class, ServiceUtility::getServiceName);
    Assertions.assertEquals("spring.application.name", exception.getProperty());
  }

  @Test
  public void shouldReturnServiceName_getServiceName() {

    String name = Gen.string().get();
    Mockito.when(environment.getProperty("spring.application.name")).thenReturn(name);

    String serviceName = ServiceUtility.getServiceName();
    Assertions.assertEquals(name, serviceName);
  }

  @Test
  public void shouldThrowException_getServiceEnvironment() {

    Mockito.when(environment.getProperty("spring.profiles.active")).thenReturn(null);

    RuntimePropertyNotFoundException exception = Assertions.assertThrows(RuntimePropertyNotFoundException.class, ServiceUtility::getServiceEnvironment);
    Assertions.assertEquals("spring.profiles.active", exception.getProperty());
  }

  @Test
  public void shouldReturnServiceEnvironment_getServiceEnvironment() {

    String env = Gen.string().get();
    Mockito.when(environment.getProperty("spring.profiles.active")).thenReturn(env);

    String serviceEnv = ServiceUtility.getServiceEnvironment();
    Assertions.assertEquals(env, serviceEnv);
  }

  @Test
  public void shouldThrowException_getServiceVersion() {

    Mockito.when(environment.getProperty("service.version")).thenReturn(null);

    RuntimePropertyNotFoundException exception = Assertions.assertThrows(RuntimePropertyNotFoundException.class, ServiceUtility::getServiceVersion);
    Assertions.assertEquals("service.version", exception.getProperty());
  }

  @Test
  public void shouldReturnServiceVersion_getServiceVersion() {

    String version = Gen.string().get();
    Mockito.when(environment.getProperty("service.version")).thenReturn(version);

    String serviceVersion = ServiceUtility.getServiceVersion();
    Assertions.assertEquals(version, serviceVersion);
  }

  @Test
  public void shouldThrowException_getServiceRegion() {

    Mockito.when(environment.getProperty("service.region")).thenReturn(null);

    RuntimePropertyNotFoundException exception = Assertions.assertThrows(RuntimePropertyNotFoundException.class, ServiceUtility::getServiceRegion);
    Assertions.assertEquals("service.region", exception.getProperty());
  }

  @Test
  public void shouldReturnServiceRegion_getServiceRegion() {

    String region = Gen.string().get();
    Mockito.when(environment.getProperty("service.region")).thenReturn(region);

    String serviceRegion = ServiceUtility.getServiceRegion();
    Assertions.assertEquals(region, serviceRegion);
  }

  @Test
  public void shouldThrowException_getServiceTenant() {

    Mockito.when(environment.getProperty("service.tenant")).thenReturn(null);

    RuntimePropertyNotFoundException exception = Assertions.assertThrows(RuntimePropertyNotFoundException.class, ServiceUtility::getServiceTenant);
    Assertions.assertEquals("service.tenant", exception.getProperty());
  }

  @Test
  public void shouldReturnServiceTenant_getServiceTenant() {

    String tenant = Gen.string().get();
    Mockito.when(environment.getProperty("service.tenant")).thenReturn(tenant);

    String serviceTenant = ServiceUtility.getServiceTenant();
    Assertions.assertEquals(tenant, serviceTenant);
  }

  @Test
  public void shouldThrowException_getServiceProperty() {

    String property = Gen.string().get();
    Mockito.when(environment.getProperty(property)).thenReturn(null);

    RuntimePropertyNotFoundException exception = Assertions.assertThrows(RuntimePropertyNotFoundException.class, () -> ServiceUtility.getServiceProperty(property));
    Assertions.assertEquals(property, exception.getProperty());
  }

  @Test
  public void shouldReturnServiceProperty_getServiceProperty() {

    String property = Gen.string().get();
    String propertyValue = Gen.string().get();

    Mockito.when(environment.getProperty(property)).thenReturn(propertyValue);

    String propertyData = ServiceUtility.getServiceProperty(property);
    Assertions.assertEquals(propertyValue, propertyData);
  }

  @ParameterizedTest
  @MethodSource("shouldReturnServicePropertyParameters")
  public void shouldReturnServicePropertyDefault_getServiceProperty(String property, String propertyValue, String defaultValue, String result) {

    Mockito.when(environment.getProperty(property)).thenReturn(propertyValue);

    String propertyData = ServiceUtility.getServiceProperty(property, defaultValue);
    Assertions.assertEquals(result, propertyData);
  }

  public static Stream<Arguments> shouldReturnServicePropertyParameters() {

    String property = Gen.string().get();
    String propertyValue = Gen.string().get();
    String defaultValue = Gen.string().get();

    return Stream.of(
        Arguments.of(property, null, null, null),
        Arguments.of(property, null, defaultValue, defaultValue),
        Arguments.of(property, propertyValue, null, propertyValue),
        Arguments.of(property, propertyValue, defaultValue, propertyValue)
    );
  }
}
