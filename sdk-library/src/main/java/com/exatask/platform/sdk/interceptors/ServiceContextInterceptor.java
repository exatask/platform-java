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

    template.header(RequestContextHeader.TRACE_ID, RequestContextProvider.getTraceId())
        .header(RequestContextHeader.PARENT_ID, RequestContextProvider.getSpanId());

    Optional.ofNullable(RequestContextProvider.getSessionToken())
        .filter(sessionToken -> !sessionToken.isEmpty())
        .ifPresent(sessionToken -> template.header(RequestContextHeader.SESSION_TOKEN, sessionToken)
            .header(RequestContextHeader.SESSION_ID, RequestContextProvider.getSessionId()));

    Optional.ofNullable(RequestContextProvider.getTenant())
        .filter(tenant -> !tenant.isEmpty())
        .ifPresent(tenant -> template.header(RequestContextHeader.TENANT, tenant));

    Optional.ofNullable(RequestContextProvider.getAccountNumber())
        .filter(accountNumber -> accountNumber > 0)
        .ifPresent(accountNumber -> template.header(RequestContextHeader.ACCOUNT_NUMBER, accountNumber.toString())
            .header(RequestContextHeader.ORGANIZATION_URN, RequestContextProvider.getOrganizationUrn())
            .header(RequestContextHeader.ORGANIZATION_NAME, RequestContextProvider.getOrganizationName()));

    Optional.ofNullable(RequestContextProvider.getEmployeeUrn())
        .filter(employeeUrn -> !employeeUrn.isEmpty())
        .ifPresent(employeeUrn -> template.header(RequestContextHeader.EMPLOYEE_URN, employeeUrn)
            .header(RequestContextHeader.EMPLOYEE_NAME, RequestContextProvider.getEmployeeName())
            .header(RequestContextHeader.EMPLOYEE_EMAIL_ID, RequestContextProvider.getEmployeeEmailId())
            .header(RequestContextHeader.EMPLOYEE_MOBILE_NUMBER, RequestContextProvider.getEmployeeMobileNumber()));
  }
}
