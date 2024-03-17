package com.exatask.platform.utilities.unit.properties;

import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.credentials.AppCredentials;
import com.exatask.platform.utilities.credentials.HttpBasicCredentials;
import com.exatask.platform.utilities.credentials.JwtHmacCredentials;
import com.exatask.platform.utilities.credentials.NoAuthCredentials;
import com.exatask.platform.utilities.properties.FeignProperties;
import com.exatask.platform.utilities.services.ServiceAuth;
import org.instancio.Gen;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.stream.Stream;

public class FeignPropertiesTest {

  @ParameterizedTest
  @MethodSource("shouldReturnHostParameters")
  public void shouldReturnHost_httpHost(String url, String host, int port, boolean secured, String result) {

    FeignProperties feignProperties = Instancio.of(FeignProperties.class)
        .set(Select.field(FeignProperties.class, "url"), url)
        .set(Select.field(FeignProperties.class, "host"), host)
        .set(Select.field(FeignProperties.class, "port"), port)
        .set(Select.field(FeignProperties.class, "secured"), secured)
        .create();

    Assertions.assertEquals(result, feignProperties.httpHost());
  }

  public static Stream<Arguments> shouldReturnHostParameters() {

    String url = Gen.string().get();
    String host = Gen.string().get();
    int port = Gen.ints().range(100, 200).get();

    return Stream.of(
        Arguments.of(url, null, 0, false, url),
        Arguments.of(null, host, port, false, "http://" + host + ":" + port),
        Arguments.of(null, host, port, true, "https://" + host + ":" + port),
        Arguments.of("", host, port, false, "http://" + host + ":" + port),
        Arguments.of("", host, port, true, "https://" + host + ":" + port)
    );
  }

  @Test
  public void shouldReturnNull_credentials() {

    FeignProperties feignProperties = Instancio.create(FeignProperties.class);
    feignProperties.setHost(null);
    feignProperties.setUrl(null);

    Assertions.assertNull(feignProperties.credentials());
  }

  @Test
  public void shouldReturnNoCredentials_credentials() {

    FeignProperties feignProperties = Instancio.create(FeignProperties.class);
    feignProperties.setAuthentication(ServiceAuth.NO_AUTH);

    AppCredentials credentials = feignProperties.credentials();
    Assertions.assertEquals(NoAuthCredentials.class, credentials.getClass());
  }

  @Test
  public void shouldReturnHttpCredentials_credentials() {

    String username = Gen.string().get();
    String password = Gen.string().get();

    FeignProperties feignProperties = Instancio.of(FeignProperties.class)
        .set(Select.field(FeignProperties.class, "authentication"), ServiceAuth.HTTP_BASIC)
        .set(Select.field(FeignProperties.class, "username"), username)
        .set(Select.field(FeignProperties.class, "password"), password)
        .create();

    HttpBasicCredentials credentials = (HttpBasicCredentials) feignProperties.credentials();

    Assertions.assertAll(
        () -> Assertions.assertEquals(username, credentials.getUsername()),
        () -> Assertions.assertEquals(password, credentials.getPassword())
    );
  }

  @Test
  public void shouldReturnJwtCredentials_credentials() {

    String service = Gen.string().get();
    String audience = Gen.string().get();
    String password = Gen.string().get();

    FeignProperties feignProperties = Instancio.of(FeignProperties.class)
        .set(Select.field(FeignProperties.class, "authentication"), ServiceAuth.JWT_HMAC)
        .set(Select.field(FeignProperties.class, "service"), audience)
        .set(Select.field(FeignProperties.class, "password"), password)
        .create();

    MockedStatic<ServiceUtility> serviceUtility = Mockito.mockStatic(ServiceUtility.class);
    serviceUtility.when(ServiceUtility::getServiceName)
        .thenReturn(service);

    JwtHmacCredentials credentials = (JwtHmacCredentials) feignProperties.credentials();

    Assertions.assertAll(
        () -> Assertions.assertEquals(service, credentials.getIssuer()),
        () -> Assertions.assertEquals(audience, credentials.getAudience()),
        () -> Assertions.assertEquals(password, credentials.getSecret())
    );

    serviceUtility.close();
  }
}
