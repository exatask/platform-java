package com.exatask.platform.logging.serializers;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class AppLogSerializerFactory {

  private static final Map<AppLogSerializerType, AppLogSerializer> serializerList = new HashMap<>();

  private static final AppLogSerializer defaultSerializer = initializeLogSerializer(AppLogSerializerType.LINE);

  public static AppLogSerializer getLogSerializer(String type) {

    try {

      AppLogSerializerType serializerType = AppLogSerializerType.valueOf(type);
      return getLogSerializer(serializerType);

    } catch (IllegalArgumentException exception) {
      return defaultSerializer;
    }
  }

  public static AppLogSerializer getLogSerializer(AppLogSerializerType type) {

    if (serializerList.containsKey(type)) {
      return serializerList.get(type);
    }

    AppLogSerializer logSerializer = initializeLogSerializer(type);
    serializerList.put(type, logSerializer);
    return logSerializer;
  }

  private static AppLogSerializer initializeLogSerializer(AppLogSerializerType type) {

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
