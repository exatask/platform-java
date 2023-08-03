package com.exatask.platform.utilities.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceZone {

  GLOBAL_01("Global", "global-01"),
  AWS_INDIA_01("AWS India", "aws-india-01");

  private final String name;

  private final String code;
}
