package com.exatask.platform.api.interceptors;

import com.exatask.platform.utilities.constants.RequestContextHeader;
import com.exatask.platform.utilities.contexts.RequestContext;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiContextInterceptor extends AppInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String traceId = StringUtils.defaultIfEmpty(request.getHeader(RequestContextHeader.TRACE_ID), UUID.randomUUID().toString());

    RequestContext.RequestContextBuilder requestContextBuilder = RequestContext.builder()
        .startTime(LocalDateTime.now())
        .traceId(traceId)
        .parentId(StringUtils.defaultIfEmpty(request.getHeader(RequestContextHeader.PARENT_ID), traceId))
        .spanId(UUID.randomUUID().toString());

    Optional.ofNullable(request.getHeader(RequestContextHeader.SESSION_TOKEN))
        .filter(sessionToken -> !sessionToken.isEmpty())
        .ifPresent(sessionToken -> requestContextBuilder.sessionToken(sessionToken)
            .sessionId(request.getHeader(RequestContextHeader.SESSION_ID)));

    Optional.ofNullable(request.getHeader(RequestContextHeader.TENANT))
        .filter(tenant -> !tenant.isEmpty())
        .ifPresent(requestContextBuilder::tenant);

    Optional.ofNullable(request.getHeader(RequestContextHeader.ACCOUNT_NUMBER))
        .filter(accountNumber -> !accountNumber.isEmpty())
        .ifPresent(accountNumber -> requestContextBuilder.accountNumber(Long.parseLong(accountNumber))
            .organizationUrn(request.getHeader(RequestContextHeader.ORGANIZATION_URN))
            .organizationName(request.getHeader(RequestContextHeader.ORGANIZATION_NAME)));

    Optional.ofNullable(request.getHeader(RequestContextHeader.EMPLOYEE_URN))
        .filter(employeeUrn -> !employeeUrn.isEmpty())
        .ifPresent(employeeUrn -> requestContextBuilder.employeeUrn(employeeUrn)
            .employeeName(request.getHeader(RequestContextHeader.EMPLOYEE_NAME))
            .employeeEmailId(request.getHeader(RequestContextHeader.EMPLOYEE_EMAIL_ID))
            .employeeMobileNumber(request.getHeader(RequestContextHeader.EMPLOYEE_MOBILE_NUMBER)));

    Optional.ofNullable(request.getHeader(RequestContextHeader.SECURITY_TARGET))
        .filter(securityTarget -> !securityTarget.isEmpty())
        .ifPresent(securityTarget -> requestContextBuilder.securityTarget(securityTarget)
            .securityOtp(request.getHeader(RequestContextHeader.SECURITY_OTP)));

    RequestContextProvider.setContext(requestContextBuilder.build());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    RequestContextProvider.unsetContext();
  }
}
