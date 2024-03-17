package com.exatask.platform.utilities.unit.serializers;

import com.exatask.platform.utilities.serializers.LocalDateTimeSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeSerializerTest {

  private static LocalDateTimeSerializer localDateTimeSerializer;

  private static JsonGenerator jsonGenerator;

  @BeforeAll
  public static void setUp() {

    jsonGenerator = Mockito.mock(JsonGenerator.class);
    localDateTimeSerializer = new LocalDateTimeSerializer();
  }

  @AfterAll
  public static void tearDown() throws IOException {
    jsonGenerator.close();
  }

  @Test
  public void shouldWriteNull_serialize() throws IOException {

    localDateTimeSerializer.serialize(null, jsonGenerator, null);

    Mockito.verify(jsonGenerator, Mockito.times(1)).writeNull();
  }

  @Test
  public void shouldWriteDateTime_serialize() throws IOException {

    LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 16, 20, 15, 30, 0);
    localDateTimeSerializer.serialize(localDateTime, jsonGenerator, null);

    Mockito.verify(jsonGenerator, Mockito.times(1)).writeString("2024-03-16T20:15:30Z");
  }
}
