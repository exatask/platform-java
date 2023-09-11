package com.exatask.platform.rabbitmq.contexts;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class HttpContext {

  private final String acceptLanguage;

  private final String ipAddress;

  private final String userAgent;

  private final String referer;
}
