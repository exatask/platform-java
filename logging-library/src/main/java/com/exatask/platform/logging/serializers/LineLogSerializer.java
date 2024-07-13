package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.properties.AppProperties;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class LineLogSerializer implements AppLogSerializer {

  private final AppProperties properties;

  public LineLogSerializer(AppProperties properties) {
    this.properties = properties;
  }

  @Override
  public String serialize(AppLogMessage logMessage) {

    switch (properties.getLength()) {

      case SMALL: return serializeSmall(logMessage);
      case MEDIUM: return serializeMedium(logMessage);
      case LONG: return serializeLong(logMessage);
    }

    return logMessage.toString();
  }

  private String serializeSmall(AppLogMessage logMessage) {

    return String.format("[%s] [%s] %s: %s (%s) [Info %s] [Trace %s]",
        logMessage.getTimestamp(),
        StringUtils.defaultString(logMessage.getTraceId(), "-"),
        logMessage.getLevel().toUpperCase(),
        logMessage.getMessage(),
        StringUtils.defaultString(logMessage.getErrorCode(), "-"),
        !MapUtils.isEmpty(logMessage.getExtraParams()) ? logMessage.getExtraParams() : "-",
        !ObjectUtils.isEmpty(logMessage.getException()) ? logMessage.getStackTrace(properties) : "-");
  }

  private String serializeMedium(AppLogMessage logMessage) {

    return String.format("[%s, %s] [%s, %s] %s: %s (%s) [%s %s] [Info %s] [Invalid Attrs %s] [Trace %s]",
        logMessage.getTimestamp(),
        logMessage.getThreadName(),
        StringUtils.defaultString(logMessage.getTraceId(), "-"),
        StringUtils.defaultString(logMessage.getSessionId(), "-"),
        logMessage.getLevel().toUpperCase(),
        logMessage.getMessage(),
        StringUtils.defaultString(logMessage.getErrorCode(), "-"),
        StringUtils.defaultString(logMessage.getMethod(), "-"),
        StringUtils.defaultString(logMessage.getUrl(), "-"),
        !MapUtils.isEmpty(logMessage.getExtraParams()) ? logMessage.getExtraParams() : "-",
        !MapUtils.isEmpty(logMessage.getInvalidAttributes()) ? logMessage.getInvalidAttributes().keySet() : "-",
        !ObjectUtils.isEmpty(logMessage.getException()) ? logMessage.getStackTrace(properties) : "-");
  }

  private String serializeLong(AppLogMessage logMessage) {

    return String.format("[%s, %s, %s] [%s, %s, %s, %s] %s: %s (%s, %s) [%s %s, %sms] [Info %s] [Invalid Attrs %s] [Trace %s]",
        logMessage.getTimestamp(),
        logMessage.getServiceName(),
        logMessage.getThreadName(),
        StringUtils.defaultString(logMessage.getTraceId(), "-"),
        StringUtils.defaultString(logMessage.getParentId(), "-"),
        StringUtils.defaultString(logMessage.getSpanId(), "-"),
        StringUtils.defaultString(logMessage.getSessionId(), "-"),
        logMessage.getLevel().toUpperCase(),
        logMessage.getMessage(),
        StringUtils.defaultString(logMessage.getErrorCode(), "-"),
        ObjectUtils.defaultIfNull(logMessage.getHttpCode(), "-"),
        StringUtils.defaultString(logMessage.getMethod(), "-"),
        StringUtils.defaultString(logMessage.getUrl(), "-"),
        ObjectUtils.defaultIfNull(logMessage.getRequestTime(), "-"),
        !MapUtils.isEmpty(logMessage.getExtraParams()) ? logMessage.getExtraParams() : "-",
        !MapUtils.isEmpty(logMessage.getInvalidAttributes()) ? logMessage.getInvalidAttributes() : "-",
        !ObjectUtils.isEmpty(logMessage.getException()) ? logMessage.getStackTrace(properties) : "-");
  }
}
