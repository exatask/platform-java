package com.exatask.platform.api.utilities;

import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.constants.services.ServiceEnvironment;
import com.exatask.platform.service.responses.HttpErrorResponse;
import com.exatask.platform.service.responses.messages.MessageType;
import com.exatask.platform.service.responses.messages.ResponseMessage;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class HttpResponseUtility {

  public static HttpErrorResponse httpErrorResponse(Exception exception) {

    HttpErrorResponse.HttpErrorResponseBuilder httpErrorResponseBuilder = HttpErrorResponse.builder();

    ResponseMessage.ResponseMessageBuilder responseMessageBuilder = ResponseMessage.builder()
        .type(MessageType.ERROR)
        .text(exception.getMessage());

    if (exception instanceof HttpException) {

      HttpException httpException = (HttpException) exception;
      responseMessageBuilder.errorCode(httpException.getError().getErrorCode());
      httpErrorResponseBuilder.invalidAttributes(httpException.getInvalidAttributes())
          .extraParams(httpException.getExtraParams());
    }

    if (ApiServiceUtility.getServiceEnvironment() != ServiceEnvironment.RELEASE) {
      httpErrorResponseBuilder.stackTrace(exception.getStackTrace());
      Optional.ofNullable(exception.getCause()).ifPresent(cause -> httpErrorResponseBuilder.exceptionCause(cause.toString()));
    }

    HttpErrorResponse httpErrorResponse = httpErrorResponseBuilder
        .message(responseMessageBuilder.build())
        .build();

    httpErrorResponse.setStatus(false);
    return httpErrorResponse;
  }
}
