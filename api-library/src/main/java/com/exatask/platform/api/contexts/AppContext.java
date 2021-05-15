package com.exatask.platform.api.contexts;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AppContext {

  private final String traceId;

  private final String sessionId;

  private final String organizationId;

  private final String organizationName;

  private final String userId;

  private final String userName;

  private final String userEmailId;
}
