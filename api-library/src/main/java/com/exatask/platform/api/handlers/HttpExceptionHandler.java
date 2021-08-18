package com.exatask.platform.api.handlers;

import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.utilities.HttpResponseUtility;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.dto.responses.HttpErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Hidden
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpExceptionHandler {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

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
    return new ResponseEntity<>(HttpResponseUtility.httpErrorResponse(exception), exception.getHttpStatus());
  }
}
