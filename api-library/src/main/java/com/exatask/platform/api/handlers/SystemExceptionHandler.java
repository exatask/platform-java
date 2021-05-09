package com.exatask.platform.api.handlers;

import com.exatask.platform.api.constants.PlatformApiService;
import com.exatask.platform.api.contexts.AppContextProvider;
import com.exatask.platform.api.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SystemExceptionHandler {

  private static final AppLogger LOGGER = AppLogManager.getLogger(PlatformApiService.LOGGER_NAME);

  private void logException(HttpServletRequest request, Exception exception, HttpStatus httpStatus) {

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setUrl(request.getRequestURI())
        .setTraceId(AppContextProvider.getTraceId())
        .setHttpCode(httpStatus.value());
    LOGGER.error(logMessage);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public HttpErrorResponse handleHttpMethodNotAllowed(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {

    logException(request, exception, HttpStatus.METHOD_NOT_ALLOWED);
    return new HttpErrorResponse(exception);
  }
}
