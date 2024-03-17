package com.exatask.platform.utilities.unit.deserializers;

import com.exatask.platform.utilities.deserializers.LocalDateTimeDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class LocalDateTimeDeserializerTest {

  private static LocalDateTimeDeserializer localDateTimeDeserializer;

  private static JsonParser jsonParser;

  @BeforeAll
  public static void setUp() {

    jsonParser = Mockito.mock(JsonParser.class);
    localDateTimeDeserializer = new LocalDateTimeDeserializer();
  }

  @AfterAll
  public static void tearDown() throws IOException {
    jsonParser.close();
  }

  @ParameterizedTest
  @MethodSource("shouldReturnDateTimeParameters")
  public void shouldReturnDateTime_deserialize(String dateTimeString, LocalDateTime dateTime) throws IOException {

    Mockito.when(jsonParser.getText()).thenReturn(dateTimeString);
    LocalDateTime localDateTime = localDateTimeDeserializer.deserialize(jsonParser, null);

    Assertions.assertEquals(dateTime, localDateTime);
  }

  public static Stream<Arguments> shouldReturnDateTimeParameters() {

    LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 16, 20, 50, 4);

    return Stream.of(
        Arguments.of(null, null),
        Arguments.of("", null),
        Arguments.of("2024-03-16T20:50:04.000Z", localDateTime),
        Arguments.of("2024-03-16T20:50:04.000+00:00", localDateTime)
    );
  }
}
