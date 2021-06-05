package com.exatask.platform.sdk.interceptors;

import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceContextInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate template) {

    template.header(RequestContextHeader.TRACE_ID, RequestContextProvider.getTraceId());
    Optional.ofNullable(RequestContextProvider.getSessionId()).ifPresent(sessionId -> template.header(
        RequestContextHeader.SESSION_ID, sessionId));

    Optional.ofNullable(RequestContextProvider.getOrganizationId()).ifPresent(organizationId -> {
      template.header(RequestContextHeader.ORGANIZATION_ID, organizationId)
          .header(RequestContextHeader.ORGANIZATION_NAME, RequestContextProvider.getOrganizationName());
    });

    Optional.ofNullable(RequestContextProvider.getUserId()).ifPresent(userId -> {
      template.header(RequestContextHeader.USER_ID, userId)
          .header(RequestContextHeader.USER_NAME, RequestContextProvider.getUserName())
          .header(RequestContextHeader.USER_EMAIL_ID, RequestContextProvider.getUserEmailId());
    });
  }
}
