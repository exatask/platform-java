package com.exatask.platform.utilities.contexts;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class AppContext {

  private final Date startTime;

  private final String traceId;

  private final String sessionId;

  private final String organizationId;

  private final String organizationName;

  private final String userId;

  private final String userName;

  private final String userEmailId;
}
