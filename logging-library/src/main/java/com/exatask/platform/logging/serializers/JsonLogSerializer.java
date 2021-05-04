package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLogSerializer implements LogSerializer {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public LogSerializerType getType() {
    return LogSerializerType.JSON;
  }

  @Override
  public String serialize(LogMessage logMessage) {

    try {
      return mapper.writeValueAsString(logMessage);
    } catch (JsonProcessingException exception) {
      return logMessage.getMessage();
    }
  }
}
