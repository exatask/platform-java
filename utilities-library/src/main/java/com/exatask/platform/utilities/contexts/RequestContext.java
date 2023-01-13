package com.exatask.platform.utilities.contexts;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class RequestContext {

  private final LocalDateTime startTime;

  private final String traceId;

  private final String parentId;

  private final String spanId;

  private final String sessionToken;

  private final String sessionId;

  private final String tenant;

  private final String organizationId;

  private final String organizationName;

  private final String employeeId;

  private final String employeeName;

  private final String employeeEmailId;

  private final String employeeMobileNumber;

  private final String securityTarget;

  private final String securityOtp;
}
