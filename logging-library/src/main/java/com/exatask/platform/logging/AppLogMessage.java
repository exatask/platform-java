package com.exatask.platform.logging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppLogMessage {

  private final static SimpleDateFormat dateFormatter;

  static {
    dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
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
  private List<StackTraceElement> stackTrace;

  public String getTimestamp() {
    return dateFormatter.format(timestamp);
  }

  @Builder
  public static AppLogMessage buildLogMessage(String message) {

    AppLogMessage logMessage = new AppLogMessage();
    Optional.ofNullable(message).ifPresent(logMessage::setMessage);
    return logMessage;
  }
}
