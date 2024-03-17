package com.exatask.platform.utilities.unit.properties;

import com.exatask.platform.utilities.properties.FeignProperties;
import org.instancio.Gen;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

  @ParameterizedTest
  @MethodSource("shouldReturnCredentialsParameters")
  public void shouldReturnCredentials_credentials() {

  }

  public static Stream<Arguments> shouldReturnCredentialsParameters() {

    return Stream.of(
        Arguments.of()
    );
  }
}
