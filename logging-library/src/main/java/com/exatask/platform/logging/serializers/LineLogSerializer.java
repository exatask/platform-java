package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.AppLogMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class LineLogSerializer implements LogSerializer {

  @Override
  public LogSerializerType getType() {
    return LogSerializerType.LINE;
  }

  @Override
  public String serialize(AppLogMessage logMessage) {

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("[")
        .append(logMessage.getTimestamp())
        .append(", ")
        .append(logMessage.getServiceName())
        .append(": ")
        .append(StringUtils.defaultString(logMessage.getTraceId(), "-"))
        .append(", ")
        .append(StringUtils.defaultString(logMessage.getSpanId(), "-"))
        .append("] ")
        .append(logMessage.getLevel().toUpperCase())
        .append(": ")
        .append(logMessage.getMessage());

    if (ObjectUtils.anyNotNull(logMessage.getErrorCode(), logMessage.getHttpCode())) {
      stringBuilder.append(" (")
          .append(StringUtils.defaultString(logMessage.getErrorCode(), "-"))
          .append(", ")
          .append(ObjectUtils.defaultIfNull(logMessage.getHttpCode(), "-"))
          .append(")");
    }

    if (ObjectUtils.anyNotNull(logMessage.getUrl(), logMessage.getRequestTime())) {
      stringBuilder.append(" [")
          .append(StringUtils.defaultString(logMessage.getMethod(), "-"))
          .append(" ")
          .append(StringUtils.defaultString(logMessage.getUrl(), "-"))
          .append(", ")
          .append(ObjectUtils.defaultIfNull(logMessage.getRequestTime(), "-"))
          .append("ms]");
    }

    if (!MapUtils.isEmpty(logMessage.getExtraParams())) {
      stringBuilder.append(" [Info ")
          .append(logMessage.getExtraParams())
          .append("]");
    }

    if (!MapUtils.isEmpty(logMessage.getInvalidAttributes())) {
      stringBuilder.append(" [Invalid Attrs ")
          .append(logMessage.getInvalidAttributes())
          .append("]");
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
