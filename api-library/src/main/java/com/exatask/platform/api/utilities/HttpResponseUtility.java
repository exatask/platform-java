package com.exatask.platform.api.utilities;

import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.service.responses.HttpErrorResponse;
import com.exatask.platform.service.responses.messages.ResponseMessage;
import com.exatask.platform.service.responses.messages.MessageType;
import com.exatask.platform.utilities.constants.Environment;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

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

    if (ApiServiceUtility.getServiceEnvironment() != Environment.RELEASE) {
      httpErrorResponseBuilder.stackTrace(exception.getStackTrace());
      httpErrorResponseBuilder.exceptionCause(ObjectUtils.defaultIfNull(exception.getCause(), "").toString());
    }

    HttpErrorResponse httpErrorResponse = httpErrorResponseBuilder
        .message(responseMessageBuilder.build())
        .build();

    httpErrorResponse.setStatus(false);
    return httpErrorResponse;
  }
}
