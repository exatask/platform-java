package com.exatask.platform.utilities.unit.contexts;

import com.exatask.platform.utilities.contexts.RequestContext;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RequestContextProviderTest {

  @BeforeEach
  public void setUpEach() {
    RequestContextProvider.unsetContext();
  }

  @Test
  public void shouldReturnPreviousContext_setContext() {

    RequestContext requestContext1 = Instancio.create(RequestContext.class);
    Assertions.assertNull(RequestContextProvider.setContext(requestContext1));

    RequestContext requestContext2 = Instancio.create(RequestContext.class);
    Assertions.assertEquals(requestContext1, RequestContextProvider.setContext(requestContext2));
  }

  @Test
  public void shouldReturnCurrentContext_getContext() {

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext, RequestContextProvider.getContext());
  }

  @Test
  public void shouldReturnStartTime_getStartTime() {

    Assertions.assertNull(RequestContextProvider.getStartTime());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getStartTime(), RequestContextProvider.getStartTime());
  }

  @Test
  public void shouldReturnTraceId_getTraceId() {

    Assertions.assertNull(RequestContextProvider.getTraceId());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getTraceId(), RequestContextProvider.getTraceId());
  }

  @Test
  public void shouldReturnParentId_getParentId() {

    Assertions.assertNull(RequestContextProvider.getParentId());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getParentId(), RequestContextProvider.getParentId());
  }

  @Test
  public void shouldReturnSpanId_getSpanId() {

    Assertions.assertNull(RequestContextProvider.getSpanId());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getSpanId(), RequestContextProvider.getSpanId());
  }

  @Test
  public void shouldReturnSessionToken_getSessionToken() {

    Assertions.assertNull(RequestContextProvider.getSessionToken());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getSessionToken(), RequestContextProvider.getSessionToken());
  }

  @Test
  public void shouldReturnSessionId_getSessionId() {

    Assertions.assertNull(RequestContextProvider.getSessionId());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getSessionId(), RequestContextProvider.getSessionId());
  }

  @Test
  public void shouldReturnTenant_getTenant() {

    Assertions.assertNull(RequestContextProvider.getTenant());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getTenant(), RequestContextProvider.getTenant());
  }

  @Test
  public void shouldReturnAccountNumber_getAccountNumber() {

    Assertions.assertNull(RequestContextProvider.getAccountNumber());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getAccountNumber(), RequestContextProvider.getAccountNumber());
  }

  @Test
  public void shouldReturnOrganizationUrn_getOrganizationUrn() {

    Assertions.assertNull(RequestContextProvider.getOrganizationUrn());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getOrganizationUrn(), RequestContextProvider.getOrganizationUrn());
  }

  @Test
  public void shouldReturnOrganizationName_getOrganizationName() {

    Assertions.assertNull(RequestContextProvider.getOrganizationName());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getOrganizationName(), RequestContextProvider.getOrganizationName());
  }

  @Test
  public void shouldReturnEmployeeUrn_getEmployeeUrn() {

    Assertions.assertNull(RequestContextProvider.getEmployeeUrn());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getEmployeeUrn(), RequestContextProvider.getEmployeeUrn());
  }

  @Test
  public void shouldReturnEmployeeName_getEmployeeName() {

    Assertions.assertNull(RequestContextProvider.getEmployeeName());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getEmployeeName(), RequestContextProvider.getEmployeeName());
  }

  @Test
  public void shouldReturnEmployeeEmailId_getEmployeeEmailId() {

    Assertions.assertNull(RequestContextProvider.getEmployeeEmailId());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getEmployeeEmailId(), RequestContextProvider.getEmployeeEmailId());
  }

  @Test
  public void shouldReturnEmployeeMobileNumber_getEmployeeMobileNumber() {

    Assertions.assertNull(RequestContextProvider.getEmployeeMobileNumber());

    RequestContext requestContext = Instancio.create(RequestContext.class);
    RequestContextProvider.setContext(requestContext);
    Assertions.assertEquals(requestContext.getEmployeeMobileNumber(), RequestContextProvider.getEmployeeMobileNumber());
  }
}
