package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.constants.LogSerializer;
import com.exatask.platform.logging.properties.AppProperties;
import lombok.experimental.UtilityClass;

import java.util.EnumMap;
import java.util.Map;

@UtilityClass
public class AppLogSerializerFactory {

  private static final Map<LogSerializer.Style, AppLogSerializer> serializerList = new EnumMap<>(LogSerializer.Style.class);

  private static final AppLogSerializer defaultSerializer = initializeLogSerializer(AppProperties.builder()
      .style(LogSerializer.Style.LINE)
      .length(LogSerializer.Length.SMALL)
      .build());

  public static AppLogSerializer getLogSerializer(AppProperties properties) {

    if (serializerList.containsKey(properties.getStyle())) {
      return serializerList.get(properties.getStyle());
    }

    AppLogSerializer logSerializer = initializeLogSerializer(properties);
    serializerList.put(properties.getStyle(), logSerializer);
    return logSerializer;
  }

  private static AppLogSerializer initializeLogSerializer(AppProperties properties) {

    switch(properties.getStyle()) {

      case LINE:
        return new LineLogSerializer(properties);
      case JSON:
        return new JsonLogSerializer(properties);
      default:
        throw new IllegalArgumentException(String.format("'%s' is not a supported log serializer", properties.getStyle().name()));
    }
  }
}
