package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.AppLogMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class LineLogSerializer implements AppLogSerializer {

  @Override
  public AppLogSerializerType getType() {
    return AppLogSerializerType.LINE;
  }

  @Override
  public String serialize(AppLogMessage logMessage) {

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(String.format("[%s, %s, %s] [%s, %s, %s, %s] %s: %s",
        logMessage.getTimestamp(),
        logMessage.getServiceName(),
        logMessage.getThreadName(),
        StringUtils.defaultString(logMessage.getTraceId(), "-"),
        StringUtils.defaultString(logMessage.getParentId(), "-"),
        StringUtils.defaultString(logMessage.getSpanId(), "-"),
        StringUtils.defaultString(logMessage.getSessionId(), "-"),
        logMessage.getLevel().toUpperCase(),
        logMessage.getMessage()));

    if (ObjectUtils.anyNotNull(logMessage.getErrorCode(), logMessage.getHttpCode())) {
      stringBuilder.append(String.format(" (%s, %s)",
          StringUtils.defaultString(logMessage.getErrorCode(), "-"),
          ObjectUtils.defaultIfNull(logMessage.getHttpCode(), "-")));
    }

    if (ObjectUtils.anyNotNull(logMessage.getUrl(), logMessage.getRequestTime())) {
      stringBuilder.append(String.format(" [%s %s, %sms]",
          StringUtils.defaultString(logMessage.getMethod(), "-"),
          StringUtils.defaultString(logMessage.getUrl(), "-"),
          ObjectUtils.defaultIfNull(logMessage.getRequestTime(), "-")));
    }

    if (!MapUtils.isEmpty(logMessage.getExtraParams())) {
      stringBuilder.append(String.format(" [Info %s]", logMessage.getExtraParams()));
    }

    if (!MapUtils.isEmpty(logMessage.getInvalidAttributes())) {
      stringBuilder.append(String.format(" [Invalid Attrs %s]", logMessage.getInvalidAttributes()));
    }

    if (!CollectionUtils.isEmpty(logMessage.getStackTrace())) {
      stringBuilder.append(" ")
          .append(logMessage.getStackTrace());
    }

    if (!ObjectUtils.isEmpty(logMessage.getExceptionCause())) {
      stringBuilder.append(" [Cause ")
          .append(logMessage.getExceptionCause().getMessage())
          .append(" ")
          .append(logMessage.getExceptionCause().getStackTrace())
          .append("]");
    }

    return stringBuilder.toString();
  }
}
