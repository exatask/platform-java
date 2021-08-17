package com.exatask.platform.api.interceptors;

import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContext;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiContextInterceptor extends AppInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    RequestContext.RequestContextBuilder requestContextBuilder = RequestContext.builder()
        .startTime(new Date())
        .traceId(request.getHeader(RequestContextHeader.TRACE_ID))
        .parentId(request.getHeader(RequestContextHeader.PARENT_ID))
        .spanId(UUID.randomUUID().toString());

    Optional.ofNullable(request.getHeader(RequestContextHeader.SESSION_ID)).ifPresent(requestContextBuilder::sessionId);

    Optional.ofNullable(request.getHeader(RequestContextHeader.ORGANIZATION_ID)).ifPresent((organizationId) ->
        requestContextBuilder
          .organizationId(organizationId)
          .organizationName(request.getHeader(RequestContextHeader.ORGANIZATION_NAME)));

    Optional.ofNullable(request.getHeader(RequestContextHeader.EMPLOYEE_ID)).ifPresent((employeeId) ->
        requestContextBuilder
          .employeeId(employeeId)
          .employeeName(request.getHeader(RequestContextHeader.EMPLOYEE_NAME))
          .employeeEmailId(request.getHeader(RequestContextHeader.EMPLOYEE_EMAIL_ID)));

    RequestContextProvider.setContext(requestContextBuilder.build());
    return true;
  }
}
