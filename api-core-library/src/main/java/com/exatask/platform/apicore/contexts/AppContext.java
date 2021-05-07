package com.exatask.platform.apicore.contexts;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AppContext {

  private final String traceId;

  private final String sessionId;

  private final String organizationId;

  private final String userId;
}
