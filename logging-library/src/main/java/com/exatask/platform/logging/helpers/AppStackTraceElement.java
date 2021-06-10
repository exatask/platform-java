package com.exatask.platform.logging.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AppStackTraceElement {

  private final String file;

  @JsonProperty("class")
  private final String clazz;

  private final String method;

  private final Integer line;

  @Override
  public String toString() {
    return String.format("[%s, %s::%s, %d]", file, clazz, method, line);
  }
}
