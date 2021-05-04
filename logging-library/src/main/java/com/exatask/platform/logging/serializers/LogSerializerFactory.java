package com.exatask.platform.logging.serializers;

import java.util.HashMap;
import java.util.Map;

public class LogSerializerFactory {

  private static final Map<LogSerializerType, LogSerializer> serializerList = new HashMap<>();

  private static final LogSerializer defaultSerializer = initializeLogSerializer(LogSerializerType.LINE);

  private LogSerializerFactory() {
  }

  public static LogSerializer getLogSerializer(String type) {

    try {

      LogSerializerType serializerType = LogSerializerType.valueOf(type);
      return getLogSerializer(serializerType);

    } catch (IllegalArgumentException exception) {
      return defaultSerializer;
    }
  }

  public static LogSerializer getLogSerializer(LogSerializerType type) {

    if (serializerList.containsKey(type)) {
      return serializerList.get(type);
    }

    LogSerializer logSerializer = initializeLogSerializer(type);
    serializerList.put(type, logSerializer);
    return logSerializer;
  }

  private static LogSerializer initializeLogSerializer(LogSerializerType type) {

    switch(type) {

      case LINE:
        return new LineLogSerializer();
      case JSON:
        return new JsonLogSerializer();
      default:
        throw new IllegalArgumentException(String.format("'%s' is not a supported log serializer", type.name()));
    }
  }
}
