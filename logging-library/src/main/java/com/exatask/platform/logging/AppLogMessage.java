package com.exatask.platform.logging;

import com.exatask.platform.logging.elements.AppStackTraceElement;
import com.exatask.platform.logging.properties.AppProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AppLogMessage {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
  private static final Pattern PACKAGES = Pattern.compile("^com\\.exatask\\.(.+)$");

  @Setter(AccessLevel.PACKAGE)
  private LocalDateTime timestamp;

  @Setter(AccessLevel.PACKAGE)
  private String level;

  @Setter(AccessLevel.PACKAGE)
  private String serviceName;

  @Setter(AccessLevel.PACKAGE)
  private String threadName;

  @Setter(AccessLevel.PACKAGE)
  private String traceId;

  @Setter(AccessLevel.PACKAGE)
  private String parentId;

  @Setter(AccessLevel.PACKAGE)
  private String spanId;

  @Setter(AccessLevel.PACKAGE)
  private String sessionId;

  private String message;

  private Integer httpCode;

  private String url;

  private String method;

  private String errorCode;

  private long requestTime;

  private Map<String, String> invalidAttributes;

  private Map<String, Object> extraParams;

  private Exception exception;

  public String getTimestamp() {

    return timestamp.atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneOffset.UTC)
        .format(DATE_TIME_FORMATTER);
  }

  public List<AppStackTraceElement> getStackTrace(AppProperties properties) {

    List<AppStackTraceElement> stackTrace = new ArrayList<>();

    for (StackTraceElement element : this.exception.getStackTrace()) {

      String className = element.getClassName();
      if (!PACKAGES.matcher(className).matches()) {
        continue;
      }

      stackTrace.add(AppStackTraceElement.builder()
          .file(element.getFileName())
          .clazz(element.getClassName())
          .method(element.getMethodName())
          .line(element.getLineNumber())
          .build());

      if (stackTrace.size() >= properties.getStackTrace()) {
        break;
      }
    }

    return stackTrace;
  }

  @Builder
  public static AppLogMessage buildLogMessage(String message, Exception exception, @Singular Map<String, Object> extraParams, @Singular Map<String, String> invalidAttributes) {

    AppLogMessage logMessage = new AppLogMessage();

    Optional.ofNullable(exception).ifPresent(ex -> {

      logMessage.setMessage(ex.getMessage());
      logMessage.setException(ex);
    });

    Optional.ofNullable(message).ifPresent(logMessage::setMessage);
    Optional.ofNullable(extraParams).ifPresent(logMessage::setExtraParams);
    Optional.ofNullable(invalidAttributes).ifPresent(logMessage::setInvalidAttributes);

    return logMessage;
  }
}
