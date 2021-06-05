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
        .spanId(UUID.randomUUID().toString());

    Optional.ofNullable(request.getHeader(RequestContextHeader.SESSION_ID)).ifPresent(requestContextBuilder::sessionId);

    Optional.ofNullable(request.getHeader(RequestContextHeader.ORGANIZATION_ID)).ifPresent((organizationId) ->
        requestContextBuilder
          .organizationId(organizationId)
          .organizationName(request.getHeader(RequestContextHeader.ORGANIZATION_NAME)));

    Optional.ofNullable(request.getHeader(RequestContextHeader.USER_ID)).ifPresent((userId) ->
        requestContextBuilder
          .userId(userId)
          .userName(request.getHeader(RequestContextHeader.USER_NAME))
          .userEmailId(request.getHeader(RequestContextHeader.USER_EMAIL_ID)));

    RequestContextProvider.setContext(requestContextBuilder.build());
    return true;
  }
}
