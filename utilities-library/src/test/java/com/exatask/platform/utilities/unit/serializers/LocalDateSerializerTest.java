package com.exatask.platform.utilities.unit.serializers;

import com.exatask.platform.utilities.serializers.LocalDateSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateSerializerTest {

  private static LocalDateSerializer localDateSerializer;

  private static JsonGenerator jsonGenerator;

  @BeforeAll
  public static void setUp() {

    jsonGenerator = Mockito.mock(JsonGenerator.class);
    localDateSerializer = new LocalDateSerializer();
  }

  @AfterAll
  public static void tearDown() throws IOException {
    jsonGenerator.close();
  }

  @Test
  public void shouldWriteNull_serialize() throws IOException {

    localDateSerializer.serialize(null, jsonGenerator, null);

    Mockito.verify(jsonGenerator, Mockito.times(1)).writeNull();
  }

  @Test
  public void shouldWriteDate_serialize() throws IOException {

    LocalDate localDate = ZonedDateTime.of(2024, 3, 16, 20, 15, 30, 0, ZoneId.of("Asia/Kolkata"))
        .toLocalDate();
    localDateSerializer.serialize(localDate, jsonGenerator, null);

    Mockito.verify(jsonGenerator, Mockito.times(1)).writeString("2024-03-16Z");
  }
}
