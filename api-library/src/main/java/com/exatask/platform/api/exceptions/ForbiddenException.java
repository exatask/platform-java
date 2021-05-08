package com.exatask.platform.api.exceptions;

import com.exatask.platform.api.errors.AppError;
import lombok.Builder;
import lombok.Singular;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class ForbiddenException extends HttpException {

  protected ForbiddenException(String message) {
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
        HttpStatus.FORBIDDEN, message, appError, errorArguments, exception, invalidAttributes,extraParams);
  }
}
