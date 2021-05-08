package com.exatask.platform.logging.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppStackTraceElement {

  @JsonProperty("file")
  private final String file;

  @JsonProperty("class")
  private final String clazz;

  @JsonProperty("method")
  private final String method;

  @JsonProperty("line")
  private final Integer line;

  @Override
  public String toString() {
    return String.format("[%s, %s::%s, %d]", file, clazz, method, line);
  }
}
