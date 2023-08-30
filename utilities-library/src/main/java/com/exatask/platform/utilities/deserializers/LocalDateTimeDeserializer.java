package com.exatask.platform.utilities.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {

    String dateTime = parser.getText();
    if (dateTime == null || StringUtils.isEmpty(dateTime)) {
      return null;
    } else {

      return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE)
          .atZone(ZoneId.of(ZoneOffset.UTC.toString()))
          .toOffsetDateTime()
          .toLocalDateTime();
    }
  }
}
