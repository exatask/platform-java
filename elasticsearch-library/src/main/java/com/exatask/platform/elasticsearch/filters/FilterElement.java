package com.exatask.platform.elasticsearch.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.elasticsearch.index.query.Operator;

@Getter
@AllArgsConstructor
public class FilterElement {

  private final String key;

  private final FilterOperation operation;

  private final Operator operator;

  private final Object value;
}
