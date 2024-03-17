package com.exatask.platform.utilities.unit;

import com.exatask.platform.utilities.ResourceUtility;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.exatask.platform.utilities.exceptions.MissingUrnPropertyException;
import com.exatask.platform.utilities.properties.UrnProperties;
import org.instancio.Gen;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.stream.Stream;

public class ResourceUtilityTest {

  private static MockedStatic<ServiceUtility> serviceUtility;
  private static MockedStatic<RequestContextProvider> requestContextProvider;

  @BeforeAll
  public static void setUp() {

    serviceUtility = Mockito.mockStatic(ServiceUtility.class);
    requestContextProvider = Mockito.mockStatic(RequestContextProvider.class);
  }

  @AfterAll
  public static void tearDown() {

    serviceUtility.close();
    requestContextProvider.close();
  }

  @ParameterizedTest()
  @MethodSource("shouldThrowExceptionFromUrnPropertiesParameters")
  public void shouldThrowExceptionFromUrnProperties_urn(UrnProperties urnProperties, String property) {

    MissingUrnPropertyException exception = Assertions.assertThrows(MissingUrnPropertyException.class, () -> ResourceUtility.urn(urnProperties));
    Assertions.assertEquals(property, exception.getProperty());
  }

  public static Stream<Arguments> shouldThrowExceptionFromUrnPropertiesParameters() {

    String service = Gen.string().get();
    String resource = Gen.string().get();

    return Stream.of(
        Arguments.of(UrnProperties.builder().build(), "Service"),
        Arguments.of(UrnProperties.builder().service(service).build(), "Resource"),
        Arguments.of(UrnProperties.builder().service(service).resource(resource).build(), "Resource ID")
    );
  }

  @ParameterizedTest()
  @MethodSource("shouldReturnUrnFromUrnPropertiesParameters")
  public void shouldReturnUrnFromUrnProperties_urn(UrnProperties urnProperties, String result) {

    Assertions.assertEquals(ResourceUtility.urn(urnProperties), result);
  }

  public static Stream<Arguments> shouldReturnUrnFromUrnPropertiesParameters() {

    String service = Gen.string().get();
    String tenant = Gen.string().get();
    Long accountNumber = Gen.longs().get();
    String resource = Gen.string().get();
    String resourceId = Gen.string().get();

    return Stream.of(
        Arguments.of(UrnProperties.builder().service(service).resource(resource).resourceId(resourceId).build(), "ern:" + service + ":::" + resource + "/" + resourceId),
        Arguments.of(UrnProperties.builder().service(service).accountNumber(accountNumber).resource(resource).resourceId(resourceId).build(), "ern:" + service + "::" + accountNumber + ":" + resource + "/" + resourceId),
        Arguments.of(UrnProperties.builder().service(service).tenant(tenant).resource(resource).resourceId(resourceId).build(), "ern:" + service + ":" + tenant + "::" + resource + "/" + resourceId),
        Arguments.of(UrnProperties.builder().service(service).tenant(tenant).accountNumber(accountNumber).resource(resource).resourceId(resourceId).build(), "ern:" + service + ":" + tenant + ":" + accountNumber + ":" + resource + "/" + resourceId)
    );
  }

  @ParameterizedTest
  @MethodSource("shouldReturnUrnFromDataParameters")
  public void shouldReturnUrnFromRequestContext_urn(String service, String tenant, Long accountNumber, String resource, String resourceId, String result) {

    serviceUtility.when(ServiceUtility::getServiceTenant).thenReturn(tenant);
    requestContextProvider.when(RequestContextProvider::getAccountNumber).thenReturn(accountNumber);

    Assertions.assertEquals(ResourceUtility.urn(service, resource, resourceId), result);
  }

  @ParameterizedTest
  @MethodSource("shouldReturnUrnFromDataParameters")
  public void shouldReturnUrnFromService_urn(String service, String tenant, Long accountNumber, String resource, String resourceId, String result) {

    serviceUtility.when(ServiceUtility::getServiceTenant).thenReturn(tenant);

    Assertions.assertEquals(ResourceUtility.urn(service, accountNumber, resource, resourceId), result);
  }

  @ParameterizedTest
  @MethodSource("shouldReturnUrnFromDataParameters")
  public void shouldReturnUrnFromData_urn(String service, String tenant, Long accountNumber, String resource, String resourceId, String result) {

    Assertions.assertEquals(ResourceUtility.urn(service, tenant, accountNumber, resource, resourceId), result);
  }

  public static Stream<Arguments> shouldReturnUrnFromDataParameters() {

    String service = Gen.string().get();
    String tenant = Gen.string().get();
    Long accountNumber = Gen.longs().get();
    String resource = Gen.string().get();
    String resourceId = Gen.string().get();

    return Stream.of(
        Arguments.of(service, null, null, resource, resourceId, "ern:" + service + ":::" + resource + "/" + resourceId),
        Arguments.of(service, tenant, null, resource, resourceId, "ern:" + service + ":" + tenant + "::" + resource + "/" + resourceId),
        Arguments.of(service, null, accountNumber, resource, resourceId, "ern:" + service + "::" + accountNumber + ":" + resource + "/" + resourceId),
        Arguments.of(service, tenant, accountNumber, resource, resourceId, "ern:" + service + ":" + tenant + ":" + accountNumber + ":" + resource + "/" + resourceId)
    );
  }

  @Test
  public void shouldReturnUrnPropertiesAsNull_parseUrn() {

    UrnProperties urnProperties = ResourceUtility.parseUrn("urn::::/");
    Assertions.assertNull(urnProperties);
  }

  @ParameterizedTest
  @MethodSource("shouldReturnUrnPropertiesParameters")
  public void shouldReturnUrnProperties_parseUrn(String urn, String service, String tenant, Long accountNumber, String resource, String resourceId) {

    UrnProperties urnProperties = ResourceUtility.parseUrn(urn);

    Assertions.assertAll(
        () -> Assertions.assertEquals(urnProperties.getService(), service),
        () -> Assertions.assertEquals(urnProperties.getTenant(), tenant),
        () -> Assertions.assertEquals(urnProperties.getAccountNumber(), accountNumber),
        () -> Assertions.assertEquals(urnProperties.getResource(), resource),
        () -> Assertions.assertEquals(urnProperties.getResourceId(), resourceId)
    );
  }

  public static Stream<Arguments> shouldReturnUrnPropertiesParameters() {

    String service = Gen.string().get();
    String tenant = Gen.string().get();
    Long accountNumber = Gen.longs().get();
    String resource = Gen.string().get();
    String resourceId = Gen.string().get();

    return Stream.of(
        Arguments.of("ern:" + service + ":::" + resource + "/" + resourceId, service, "", null, resource, resourceId),
        Arguments.of("ern:" + service + ":" + tenant + "::" + resource + "/" + resourceId, service, tenant, null, resource, resourceId),
        Arguments.of("ern:" + service + "::" + accountNumber + ":" + resource + "/" + resourceId, service, "", accountNumber, resource, resourceId),
        Arguments.of("ern:" + service + ":" + tenant + ":" + accountNumber + ":" + resource + "/" + resourceId, service, tenant, accountNumber, resource, resourceId)
    );
  }
}
