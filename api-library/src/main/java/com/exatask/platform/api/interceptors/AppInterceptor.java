package com.exatask.platform.api.interceptors;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppInterceptor implements HandlerInterceptor {

  protected static final AppLogger LOGGER = AppLogManager.getLogger(ApiService.LOGGER_NAME);

  private void logInterceptorException(Exception exception, HttpServletRequest request) {

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setUrl(request.getRequestURI());
    LOGGER.error(logMessage);
  }

  protected void sendPreHandleErrorResponse(Exception exception, HttpServletRequest request, HttpServletResponse response) {

    logInterceptorException(exception, request);

    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    if (exception instanceof HttpException) {
      httpStatus = ((HttpException) exception).getHttpStatus();
    }

    HttpErrorResponse errorResponse = new HttpErrorResponse(exception);
    response.setContentType(MediaType.APPLICATION_JSON.toString());
    response.setStatus(httpStatus.value());

    ObjectMapper mapper = new ObjectMapper();
    try {
      response.getWriter().write(mapper.writeValueAsString(errorResponse));
    } catch (IOException ex) {
      logInterceptorException(ex, request);
    }
  }
}
