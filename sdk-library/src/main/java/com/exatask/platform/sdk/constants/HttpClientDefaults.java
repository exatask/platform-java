package com.exatask.platform.sdk.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpClientDefaults {

  public static final int PING_INTERVAL = 1;
  public static final int CONNECTION_TIMEOUT = 1;
  public static final int CALL_TIMEOUT = 5;

  public static final int MAX_IDLE_CONNECTION = 2;
  public static final int KEEP_ALIVE_DURATION = 5;
}
