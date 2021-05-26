package com.exatask.platform.api.exceptions;

import com.exatask.platform.api.errors.AppError;
import lombok.Builder;
import lombok.Singular;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class UnprocessableEntityException extends HttpException {

  protected UnprocessableEntityException(String message) {
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
        HttpStatus.UNPROCESSABLE_ENTITY, message, appError, errorArguments, exception, invalidAttributes,extraParams);
  }
}
