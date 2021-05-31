package com.exatask.platform.api.handlers;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.api.exceptions.BadRequestException;
import com.exatask.platform.api.exceptions.ForbiddenException;
import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.exceptions.InternalServerErrorException;
import com.exatask.platform.api.exceptions.MethodNotAllowedException;
import com.exatask.platform.api.exceptions.NotFoundException;
import com.exatask.platform.api.exceptions.PayloadTooLargeException;
import com.exatask.platform.api.exceptions.PreconditionFailedException;
import com.exatask.platform.api.exceptions.ServiceUnavailableException;
import com.exatask.platform.api.exceptions.TooManyRequestsException;
import com.exatask.platform.api.exceptions.UnauthorizedException;
import com.exatask.platform.api.exceptions.UnsupportedMediaTypeException;
import com.exatask.platform.api.responses.HttpErrorResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public HttpErrorResponse handleBadRequest(HttpServletRequest request, BadRequestException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(ForbiddenException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public HttpErrorResponse handleForbidden(HttpServletRequest request, ForbiddenException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(MethodNotAllowedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  public HttpErrorResponse handleMethodNotAllowed(HttpServletRequest request, MethodNotAllowedException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public HttpErrorResponse handleNotFound(HttpServletRequest request, NotFoundException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(PreconditionFailedException.class)
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  @ResponseBody
  public HttpErrorResponse handlePreconditionFailed(HttpServletRequest request, PreconditionFailedException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public HttpErrorResponse handleServerError(HttpServletRequest request, InternalServerErrorException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(ServiceUnavailableException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ResponseBody
  public HttpErrorResponse handleServiceUnavailable(HttpServletRequest request, ServiceUnavailableException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public HttpErrorResponse handleUnauthorized(HttpServletRequest request, UnauthorizedException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(TooManyRequestsException.class)
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  @ResponseBody
  public HttpErrorResponse handleTooManyRequests(HttpServletRequest request, TooManyRequestsException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(UnsupportedMediaTypeException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ResponseBody
  public HttpErrorResponse handleUnsupportedMediaType(HttpServletRequest request, UnsupportedMediaTypeException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }

  @ExceptionHandler(PayloadTooLargeException.class)
  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  @ResponseBody
  public HttpErrorResponse handlePayloadTooLarge(HttpServletRequest request, PayloadTooLargeException exception) {

    logException(request, exception);
    return new HttpErrorResponse(exception);
  }
}
