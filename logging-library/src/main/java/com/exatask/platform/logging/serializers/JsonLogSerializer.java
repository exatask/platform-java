package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.AppLogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLogSerializer implements AppLogSerializer {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public AppLogSerializerType getType() {
    return AppLogSerializerType.JSON;
  }

  @Override
  public String serialize(AppLogMessage logMessage) {

    try {
      return mapper.writeValueAsString(logMessage);
    } catch (JsonProcessingException exception) {
      return logMessage.getMessage();
    }
  }
}
