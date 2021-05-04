package com.exatask.platform.mongodb.constants;

import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

public class Defaults {

  public static final Integer DEFAULT_PAGE = 1;
  public static final Integer DEFAULT_SKIP = 0;
  public static final Integer DEFAULT_LIMIT = 25;
  public static final Integer MAXIMUM_LIMIT = 100;

  public static final Map<String, Sort.Direction> DEFAULT_SORT = new HashMap<>();

  static {
    Defaults.DEFAULT_SORT.put("_id", Sort.Direction.DESC);
  }
}
