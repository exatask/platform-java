package com.exatask.platform.api.utilities;

import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.constants.services.ServiceEnvironment;
import com.exatask.platform.dto.responses.HttpErrorResponse;
import com.exatask.platform.dto.responses.messages.MessageType;
import com.exatask.platform.dto.responses.messages.ResponseMessage;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class HttpResponseUtility {

  public static HttpErrorResponse httpErrorResponse(Exception exception) {

    HttpErrorResponse httpErrorResponse = new HttpErrorResponse();

    ResponseMessage responseMessage = new ResponseMessage();
    responseMessage.setType(MessageType.ERROR)
        .setText(exception.getMessage());

    if (exception instanceof HttpException) {

      HttpException httpException = (HttpException) exception;
      Optional.ofNullable(httpException.getError()).ifPresent(error -> responseMessage.setErrorCode(error.getErrorCode()));
      httpErrorResponse.setInvalidAttributes(httpException.getInvalidAttributes())
          .setExtraParams(httpException.getExtraParams());
    }

    if (ApiServiceUtility.getServiceEnvironment() != ServiceEnvironment.RELEASE) {
      httpErrorResponse.setStackTrace(exception.getStackTrace());
      Optional.ofNullable(exception.getCause()).ifPresent(cause -> httpErrorResponse.setExceptionCause(cause.toString()));
    }

    httpErrorResponse.setMessage(responseMessage)
        .setStatus(false);
    return httpErrorResponse;
  }
}
