package com.exatask.platform.logging.properties;

import com.exatask.platform.logging.constants.LogSerializer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppProperties {

  private LogSerializer.Style style;

  private LogSerializer.Length length;

  private Integer stackTrace;
}
