package com.exatask.platform.api.handlers;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.api.errors.CommonError;
import com.exatask.platform.api.exceptions.BadRequestException;
import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @ExceptionHandler({
      MissingServletRequestParameterException.class,
      HttpRequestMethodNotSupportedException.class,
      IllegalArgumentException.class,
      MethodArgumentNotValidException.class
  })
  @ResponseBody
  public ResponseEntity<HttpErrorResponse> handleSystemException(HttpServletRequest request, Exception exception) throws Exception {

    if (exception instanceof MissingServletRequestParameterException) {

      return handleException(request, exception, HttpStatus.BAD_REQUEST);

    } else if (exception instanceof HttpRequestMethodNotSupportedException) {

      return handleException(request, exception, HttpStatus.METHOD_NOT_ALLOWED);

    } else if (exception instanceof IllegalArgumentException) {

      return handleException(request, exception, HttpStatus.NOT_IMPLEMENTED);

    } else if (exception instanceof MethodArgumentNotValidException) {

      return handleMethodArgumentNotValidException(request, (MethodArgumentNotValidException) exception);

    } else {

      throw exception;
    }
  }

  private ResponseEntity<HttpErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception) {

    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
    Map<String, String> invalidAttributes = new HashMap<>();

    for (FieldError error : fieldErrors) {
      invalidAttributes.put(error.getField(), error.getDefaultMessage());
    }

    HttpException httpException = BadRequestException.builder()
        .appError(CommonError.INVALID_REQUEST_DATA)
        .exception(exception)
        .invalidAttributes(invalidAttributes)
        .build();

    return handleException(request, httpException);
  }

  private ResponseEntity<HttpErrorResponse> handleException(HttpServletRequest request, Exception exception, HttpStatus httpStatus) {

    logException(request, exception, httpStatus);
    return new ResponseEntity<>(new HttpErrorResponse(exception), httpStatus);
  }

  private ResponseEntity<HttpErrorResponse> handleException(HttpServletRequest request, HttpException exception) {

    logException(request, exception);
    return new ResponseEntity<>(new HttpErrorResponse(exception), exception.getHttpStatus());
  }
}
