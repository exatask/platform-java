package com.exatask.platform.logging;

import com.exatask.platform.logging.helpers.AppExceptionCause;
import com.exatask.platform.logging.helpers.AppStackTraceElement;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppLogMessage {

  private final static SimpleDateFormat DATE_FORMATTER;

  private final static Pattern PACKAGES = Pattern.compile("^com\\.exatask\\.(.+)$");

  static {
    DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @JsonProperty("timestamp")
  @Setter(AccessLevel.PACKAGE)
  private Date timestamp;

  @JsonProperty("service_name")
  @Setter(AccessLevel.PACKAGE)
  private String serviceName;

  @JsonProperty("trace_id")
  private String traceId;

  @JsonProperty("level")
  @Setter(AccessLevel.PACKAGE)
  private String level;

  @JsonProperty("message")
  private String message;

  @JsonProperty("http_code")
  private Integer httpCode;

  @JsonProperty("url")
  private String url;

  @JsonProperty("error_code")
  private String errorCode;

  @JsonProperty("request_time")
  private Long requestTime;

  @JsonProperty("invalid_attributes")
  private Map<String, String> invalidAttributes;

  @JsonProperty("extra_params")
  private Map<String, Object> extraParams;

  @JsonProperty("stack_trace")
  private List<AppStackTraceElement> stackTrace;

  @JsonProperty("exception_cause")
  private AppExceptionCause exceptionCause;

  public String getTimestamp() {
    return DATE_FORMATTER.format(timestamp);
  }

  @Builder
  public static AppLogMessage buildLogMessage(String message, Exception exception) {

    AppLogMessage logMessage = new AppLogMessage();
    Optional.ofNullable(message).ifPresent(logMessage::setMessage);

    Optional.ofNullable(exception).ifPresent((ex) -> {

      logMessage.setMessage(ex.getMessage());
      logMessage.setStackTrace(parseStackTrace(Arrays.asList(ex.getStackTrace())));

      Optional.ofNullable(ex.getCause()).ifPresent((cause) -> {

        List<StackTraceElement> elements = Arrays.asList(cause.getStackTrace());
        logMessage.setExceptionCause(new AppExceptionCause(cause.getMessage(), parseStackTrace(elements)));
      });
    });

    return logMessage;
  }

  private static List<AppStackTraceElement> parseStackTrace(List<StackTraceElement> elements) {

    List<AppStackTraceElement> stackTrace = new ArrayList<>();

    for (StackTraceElement element : elements) {

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
    }

    return stackTrace;
  }
}
