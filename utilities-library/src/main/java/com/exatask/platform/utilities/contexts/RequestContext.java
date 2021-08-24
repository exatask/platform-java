package com.exatask.platform.utilities.contexts;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder(toBuilder = true)
public class RequestContext {

  private final Date startTime;

  private final String traceId;

  private final String parentId;

  private final String spanId;

  private final String sessionId;

  private final String organizationId;

  private final String organizationName;

  private final String employeeId;

  private final String employeeName;

  private final String employeeEmailId;

  private final String securityOtp;
}
