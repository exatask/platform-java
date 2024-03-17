package com.exatask.platform.utilities.unit.deserializers;

import com.exatask.platform.utilities.deserializers.LocalDateDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Stream;

public class LocalDateDeserializerTest {

  private static LocalDateDeserializer localDateDeserializer;

  private static JsonParser jsonParser;

  @BeforeAll
  public static void setUp() {

    jsonParser = Mockito.mock(JsonParser.class);
    localDateDeserializer = new LocalDateDeserializer();
  }

  @AfterAll
  public static void tearDown() throws IOException {
    jsonParser.close();
  }

  @ParameterizedTest
  @MethodSource("shouldReturnDateParameters")
  public void shouldReturnDate_deserialize(String dateString, LocalDate date) throws IOException {

    Mockito.when(jsonParser.getText()).thenReturn(dateString);
    LocalDate localDate = localDateDeserializer.deserialize(jsonParser, null);

    Assertions.assertEquals(date, localDate);
  }

  public static Stream<Arguments> shouldReturnDateParameters() {

    LocalDate localDate = LocalDate.of(2024, 3, 16);

    return Stream.of(
        Arguments.of(null, null),
        Arguments.of("", null),
        Arguments.of("2024-03-16Z", localDate),
        Arguments.of("2024-03-16+00:00", localDate)
    );
  }
}
