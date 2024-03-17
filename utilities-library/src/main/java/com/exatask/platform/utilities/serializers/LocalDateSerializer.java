package com.exatask.platform.utilities.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

  @Override
  public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider serializers) throws IOException {

    if (value == null) {
      generator.writeNull();
    } else {

      generator.writeString(value.atStartOfDay(ZoneOffset.UTC)
          .toOffsetDateTime()
          .format(DateTimeFormatter.ISO_OFFSET_DATE));
    }
  }
}
