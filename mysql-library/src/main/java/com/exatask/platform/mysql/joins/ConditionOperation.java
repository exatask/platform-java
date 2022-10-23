package com.exatask.platform.mysql.joins;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionOperation {

  EQUAL("="),
  NOT_EQUAL("!="),
  GREATER(">"),
  GREATER_EQUAL(">="),
  LESSER("<"),
  LESSER_EQUAL("<=");

  private final String operation;
}
