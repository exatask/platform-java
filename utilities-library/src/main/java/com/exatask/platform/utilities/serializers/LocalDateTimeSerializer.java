package com.exatask.platform.utilities.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

  @Override
  public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {

    if (value == null) {
      generator.writeNull();
    } else {

      generator.writeString(value.atZone(ZoneOffset.UTC)
          .toOffsetDateTime()
          .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
  }
}
