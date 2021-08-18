package com.exatask.platform.api.handlers;

import com.exatask.platform.api.utilities.HttpResponseUtility;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.dto.responses.HttpErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private void logException(HttpServletRequest request, Exception exception) {

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setUrl(request.getRequestURI())
        .setMethod(request.getMethod())
        .setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    LOGGER.error(logMessage);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<HttpErrorResponse> handleGlobalException(HttpServletRequest request, Exception exception) {

    logException(request, exception);
    return new ResponseEntity<>(HttpResponseUtility.httpErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
