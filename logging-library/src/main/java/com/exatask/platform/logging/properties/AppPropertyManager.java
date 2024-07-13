package com.exatask.platform.logging.properties;

import com.exatask.platform.logging.constants.LogSerializer;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.lookup.StrLookup;

public class AppPropertyManager {

  @Getter
  private static AppProperties loggerProperties;

  public static AppProperties parse(LoggerContext context) {

    if (loggerProperties != null) {
      return loggerProperties;
    }

    StrLookup properties = context.getConfiguration().getStrSubstitutor().getVariableResolver();

    loggerProperties = AppProperties.builder()
        .style(parseStyle(properties.lookup("style")))
        .length(parseLength(properties.lookup("length")))
        .stackTrace(parseStackTrace(properties.lookup("stackTrace")))
        .build();
    return loggerProperties;
  }

  private static LogSerializer.Style parseStyle(String style) {

    if (StringUtils.isEmpty(style)) {
      style = LogSerializer.Style.LINE.toString();
    }
    return LogSerializer.Style.valueOf(style);
  }

  private static LogSerializer.Length parseLength(String length) {

    if (StringUtils.isEmpty(length)) {
      length = LogSerializer.Length.SMALL.toString();
    }
    return LogSerializer.Length.valueOf(length);
  }

  private static int parseStackTrace(String stackTrace) {

    if (StringUtils.isEmpty(stackTrace)) {
      stackTrace = Integer.toString(5);
    }
    return Integer.parseInt(stackTrace);
  }
}
