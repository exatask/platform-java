package com.exatask.platform.api.interceptors;

import com.exatask.platform.api.constants.ContextHeader;
import com.exatask.platform.api.contexts.AppContext;
import com.exatask.platform.api.contexts.AppContextProvider;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class ApiContextInterceptor extends AppInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    AppContext.AppContextBuilder appContextBuilder = AppContext.builder()
        .traceId(request.getHeader(ContextHeader.TRACE_ID));

    Optional.of(request.getHeader(ContextHeader.SESSION_ID)).ifPresent(appContextBuilder::sessionId);

    Optional.of(request.getHeader(ContextHeader.ORGANIZATION_ID)).ifPresent((organizationId) ->
        appContextBuilder
          .organizationId(organizationId)
          .organizationName(request.getHeader(ContextHeader.ORGANIZATION_NAME)));

    Optional.of(request.getHeader(ContextHeader.USER_ID)).ifPresent((userId) ->
        appContextBuilder
          .userId(userId)
          .userName(request.getHeader(ContextHeader.USER_NAME))
          .userEmailId(request.getHeader(ContextHeader.USER_EMAIL_ID)));

    AppContextProvider.setContext(appContextBuilder.build());
    return super.preHandle(request, response, handler);
  }
}
