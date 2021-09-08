package com.exatask.platform.api.handlers;

import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.exceptions.InternalServerErrorException;
import com.exatask.platform.api.utilities.HttpResponseUtility;
import com.exatask.platform.dto.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.CollectionUtility;
import com.exatask.platform.utilities.exceptions.SdkException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpExceptionHandler {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private void logException(HttpServletRequest request, HttpException exception) {

    String errorCode = null;
    if (ObjectUtils.isNotEmpty(exception.getError())) {
      errorCode = exception.getError().getErrorCode();
    }

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setUrl(request.getRequestURI())
        .setMethod(request.getMethod())
        .setHttpCode(exception.getHttpStatus().value())
        .setErrorCode(errorCode)
        .setInvalidAttributes(exception.getInvalidAttributes())
        .setExtraParams(exception.getExtraParams());
    LOGGER.error(logMessage);
  }

  private void logException(HttpServletRequest request, SdkException exception, HttpErrorResponse errorResponse) {

    Map<String, String> serviceDetails = new HashMap<>();
    serviceDetails.put("url", exception.getUrl());
    serviceDetails.put("method", exception.getMethod());
    serviceDetails.put("method_key", exception.getMethodKey());

    Map<String, Object> extraParams = CollectionUtility.defaultIfNull(errorResponse.getExtraParams(), new HashMap<>());
    extraParams.put("service_details", serviceDetails);

    String errorCode = null;
    AppLogMessage.AppLogMessageBuilder logMessageBuilder = AppLogMessage.builder()
        .exception(exception);

    if (ObjectUtils.isNotEmpty(errorResponse.getMessage())) {

      logMessageBuilder.message(errorResponse.getMessage().getText());
      errorCode = errorResponse.getMessage().getErrorCode();
    }

    AppLogMessage logMessage = logMessageBuilder.build();
    logMessage.setUrl(request.getRequestURI())
        .setMethod(request.getMethod())
        .setHttpCode(exception.getStatusCode())
        .setErrorCode(errorCode)
        .setInvalidAttributes(errorResponse.getInvalidAttributes())
        .setExtraParams(extraParams);
    LOGGER.error(logMessage);
  }

  @ExceptionHandler(HttpException.class)
  @ResponseBody
  public ResponseEntity<HttpErrorResponse> handleHttpException(HttpServletRequest request, HttpException exception) {

    logException(request, exception);
    return new ResponseEntity<>(HttpResponseUtility.httpErrorResponse(exception), exception.getHttpStatus());
  }

  @ExceptionHandler(SdkException.class)
  @ResponseBody
  public ResponseEntity<HttpErrorResponse> handleSdkException(HttpServletRequest request, SdkException exception) {

    try {

      HttpErrorResponse errorResponse = MAPPER.readValue(exception.getErrorResponse(), HttpErrorResponse.class);
      logException(request, exception, errorResponse);
      return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getStatusCode()));

    } catch (IOException ioException) {

      LOGGER.error(ioException);
      HttpException httpException = InternalServerErrorException.builder()
          .message(exception.getErrorResponse())
          .exception(ioException)
          .build();

      logException(request, httpException);
      return new ResponseEntity<>(HttpResponseUtility.httpErrorResponse(httpException), httpException.getHttpStatus());
    }
  }
}
