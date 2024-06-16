package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.properties.AppProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class JsonLogSerializer implements AppLogSerializer {

  private final AppProperties properties;

  private final ObjectMapper mapper;

  public JsonLogSerializer(AppProperties properties) {

    this.properties = properties;
    mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule(getSerializerClass(), new Version(1, 0, 0, null, null, null));
    module.addSerializer(AppLogMessage.class, getSerializer());
    mapper.registerModule(module);
  }

  @Override
  public String serialize(AppLogMessage logMessage) {

    try {
      return mapper.writeValueAsString(logMessage);
    } catch (JsonProcessingException exception) {
      return logMessage.getMessage();
    }
  }

  private String getSerializerClass() {

    switch (properties.getLength()) {

      case SMALL: return JsonSmallLogSerializer.class.getTypeName();
      case MEDIUM: return JsonMediumLogSerializer.class.getTypeName();
      case LONG: return JsonLongLogSerializer.class.getTypeName();
    }

    return null;
  }

  private StdSerializer<AppLogMessage> getSerializer() {

    switch (properties.getLength()) {

      case SMALL: new JsonSmallLogSerializer(AppLogMessage.class);
      case MEDIUM: return new JsonMediumLogSerializer(AppLogMessage.class);
      case LONG: return new JsonLongLogSerializer(AppLogMessage.class);
    }

    return null;
  }

  static class JsonSmallLogSerializer extends StdSerializer<AppLogMessage> {

    public JsonSmallLogSerializer(Class<AppLogMessage> clazz) {
      super(clazz);
    }

    @Override
    public void serialize(AppLogMessage logMessage, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

      Map<String, String> exceptionCause = Collections.singletonMap("cause", logMessage.getExceptionCause().getMessage());

      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("timestamp", logMessage.getTimestamp());
      jsonGenerator.writeStringField("traceId", logMessage.getTraceId());
      jsonGenerator.writeStringField("level", logMessage.getLevel().toUpperCase());
      jsonGenerator.writeStringField("message", logMessage.getMessage());
      jsonGenerator.writeStringField("errorCode", logMessage.getErrorCode());
      jsonGenerator.writeObjectField("extraParams", logMessage.getExtraParams());
      jsonGenerator.writeObjectField("stackTrace", logMessage.getStackTrace());
      jsonGenerator.writeObjectField("exception", exceptionCause);
      jsonGenerator.writeEndObject();
    }
  }

  static class JsonMediumLogSerializer extends JsonSmallLogSerializer {

    public JsonMediumLogSerializer(Class<AppLogMessage> clazz) {
      super(clazz);
    }

    @Override
    public void serialize(AppLogMessage logMessage, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

      super.serialize(logMessage, jsonGenerator, serializerProvider);

      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("threadName", logMessage.getThreadName());
      jsonGenerator.writeStringField("sessionId", logMessage.getSessionId());
      jsonGenerator.writeStringField("method", logMessage.getMethod());
      jsonGenerator.writeStringField("url", logMessage.getUrl());
      jsonGenerator.writeObjectField("invalidAttributes", logMessage.getInvalidAttributes().keySet());
      jsonGenerator.writeEndObject();
    }
  }

  static class JsonLongLogSerializer extends JsonMediumLogSerializer {

    public JsonLongLogSerializer(Class<AppLogMessage> clazz) {
      super(clazz);
    }

    @Override
    public void serialize(AppLogMessage logMessage, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

      super.serialize(logMessage, jsonGenerator, serializerProvider);

      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("serviceName", logMessage.getServiceName());
      jsonGenerator.writeStringField("parentId", logMessage.getParentId());
      jsonGenerator.writeStringField("spanId", logMessage.getSpanId());
      jsonGenerator.writeNumberField("httpCode", logMessage.getHttpCode());
      jsonGenerator.writeNumberField("requestTime", logMessage.getRequestTime());
      jsonGenerator.writeObjectField("invalidAttributes", logMessage.getInvalidAttributes());
      jsonGenerator.writeObjectField("exception", logMessage.getExceptionCause());
      jsonGenerator.writeEndObject();
    }
  }
}
