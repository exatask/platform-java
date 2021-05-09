package com.exatask.platform.logging.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppExceptionCause {

  @JsonProperty("message")
  private final String message;

  @JsonProperty("stack_trace")
  private final List<AppStackTraceElement> stackTrace;
}
