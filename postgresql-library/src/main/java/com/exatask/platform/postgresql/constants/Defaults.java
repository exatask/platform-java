package com.exatask.platform.postgresql.constants;

import java.util.Collections;
import java.util.Map;

public class Defaults {

  public static final Integer DEFAULT_PAGE = 1;
  public static final Integer DEFAULT_SKIP = 0;
  public static final Integer DEFAULT_LIMIT = 25;

  public static final Integer MINIMUM_SKIP = 0;
  public static final Integer MINIMUM_LIMIT = 0;

  public static final Integer MAXIMUM_LIMIT = 100;

  public static final Map<String, Integer> DEFAULT_SORT = Collections.singletonMap("id", -1);
}
