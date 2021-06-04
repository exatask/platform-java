package com.exatask.platform.api.handlers;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Hidden
@RestControllerAdvice
public class HttpExceptionHandler {

  private static final AppLogger LOGGER = AppLogManager.getLogger(ApiService.LOGGER_NAME);

  private void logException(HttpServletRequest request, HttpException exception) {

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setUrl(request.getRequestURI())
        .setMethod(request.getMethod())
        .setHttpCode(exception.getHttpStatus().value())
        .setErrorCode(exception.getError().getErrorCode())
        .setInvalidAttributes(exception.getInvalidAttributes())
        .setExtraParams(exception.getExtraParams());
    LOGGER.error(logMessage);
  }

  @ExceptionHandler(HttpException.class)
  @ResponseBody
  public ResponseEntity<HttpErrorResponse> handleHttpException(HttpServletRequest request, HttpException exception) {

    logException(request, exception);
    return new ResponseEntity<>(new HttpErrorResponse(exception), exception.getHttpStatus());
  }
}
