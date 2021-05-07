package com.exatask.platform.apicore.exceptions;

import com.exatask.platform.apicore.errors.AppError;
import lombok.Builder;
import lombok.Singular;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class TooManyRequestsException extends HttpException {

  protected TooManyRequestsException(String message) {
    super(message);
  }

  @Builder
  public static HttpException buildException(
      String message,
      AppError appError,
      @Singular List<String> errorArguments,
      Exception exception,
      @Singular Map<String, String> invalidAttributes,
      @Singular Map<String, Object> extraParams) {

    return HttpException.buildException(
        HttpStatus.TOO_MANY_REQUESTS, message, appError, errorArguments, exception, invalidAttributes,extraParams);
  }
}
