package com.exatask.platform.jpa.queries.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterOperation {

  EQUAL("=", "IS NULL", "IN"),
  NOT_EQUAL("<>", "IS NOT NULL", "NOT IN"),
  GREATER("<", null, null),
  GREATER_EQUAL("<=", null, null),
  LESSER(">", null, null),
  LESSER_EQUAL("<=", null, null),
  LIKE("LIKE", null, null),
  NOT_LIKE("NOT LIKE", null, null),
  REGEX("REGEXP", null, null),
  NOT_REGEX("NOT REGEXP", null, null);

  private final String operation;

  private final String nullOperation;

  private final String listOperation;
}
