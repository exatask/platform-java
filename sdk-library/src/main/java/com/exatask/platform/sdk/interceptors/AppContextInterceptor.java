package com.exatask.platform.sdk.interceptors;

import com.exatask.platform.utilities.constants.ContextHeader;
import com.exatask.platform.utilities.contexts.AppContextProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppContextInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate template) {

    template.header(ContextHeader.TRACE_ID, AppContextProvider.getTraceId());
    Optional.ofNullable(AppContextProvider.getSessionId()).ifPresent(sessionId -> template.header(ContextHeader.SESSION_ID, sessionId));

    Optional.ofNullable(AppContextProvider.getOrganizationId()).ifPresent(organizationId -> {
      template.header(ContextHeader.ORGANIZATION_ID, organizationId)
          .header(ContextHeader.ORGANIZATION_NAME, AppContextProvider.getOrganizationName());
    });

    Optional.ofNullable(AppContextProvider.getUserId()).ifPresent(userId -> {
      template.header(ContextHeader.USER_ID, userId)
          .header(ContextHeader.USER_NAME, AppContextProvider.getUserName())
          .header(ContextHeader.USER_EMAIL_ID, AppContextProvider.getUserEmailId());
    });
  }
}
