package com.exatask.platform.api.handlers;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.api.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Hidden
@ControllerAdvice
public class SystemExceptionHandler {

  private static final AppLogger LOGGER = AppLogManager.getLogger(ApiService.LOGGER_NAME);

  private void logException(HttpServletRequest request, Exception exception, HttpStatus httpStatus) {

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setUrl(request.getRequestURI())
        .setMethod(request.getMethod())
        .setHttpCode(httpStatus.value());
    LOGGER.error(logMessage);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  public HttpErrorResponse handleHttpMethodNotAllowed(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {

    logException(request, exception, HttpStatus.METHOD_NOT_ALLOWED);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
  @ResponseBody
  public HttpErrorResponse handleNotImplemented(HttpServletRequest request, IllegalArgumentException exception) {

    logException(request, exception, HttpStatus.NOT_IMPLEMENTED);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public HttpErrorResponse handleInternalServerError(HttpServletRequest request, RuntimeException exception) {

    logException(request, exception, HttpStatus.INTERNAL_SERVER_ERROR);
    return new HttpErrorResponse(exception);
  }
}
