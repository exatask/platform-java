package com.exatask.platform.utilities.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

  @Override
  public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {

    String dateTime = parser.getText();
    if (StringUtils.isEmpty(dateTime)) {
      return null;
    } else {

      return LocalDate.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE)
          .atStartOfDay(ZoneId.of(ZoneOffset.UTC.toString()))
          .toOffsetDateTime()
          .toLocalDate();
    }
  }
}
