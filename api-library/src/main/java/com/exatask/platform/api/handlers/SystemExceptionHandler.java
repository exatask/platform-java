package com.exatask.platform.api.handlers;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.api.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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

  private Boolean isBadRequest(Exception exception) {

    return (exception instanceof MissingServletRequestParameterException);
  }

  private Boolean isMethodNotAllowed(Exception exception) {

    return (exception instanceof HttpRequestMethodNotSupportedException);
  }

  private Boolean isNotImplemented(Exception exception) {

    return (exception instanceof IllegalArgumentException);
  }

  private HttpStatus exceptionHttpStatus(Exception exception) {

    if (isBadRequest(exception)) {
      return HttpStatus.BAD_REQUEST;
    } else if (isMethodNotAllowed(exception)) {
      return HttpStatus.METHOD_NOT_ALLOWED;
    } else if (isNotImplemented(exception)) {
      return HttpStatus.NOT_IMPLEMENTED;
    }

    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  @ExceptionHandler
  @ResponseBody
  public ResponseEntity<HttpErrorResponse> handleSystemException(HttpServletRequest request, Exception exception) {

    HttpStatus httpStatus = exceptionHttpStatus(exception);
    logException(request, exception, httpStatus);
    return new ResponseEntity<>(new HttpErrorResponse(exception), httpStatus);
  }
}
