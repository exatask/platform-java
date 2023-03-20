package com.exatask.platform.rabbitmq.exceptions;

import com.exatask.platform.utilities.errors.AppError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ListenerException extends RuntimeException {

  private AppError error;

  private Map<String, String> invalidAttributes;

  private Map<String, Object> extraParams;

  protected ListenerException(String message) {
    super(message);
  }

  protected ListenerException(String message, Throwable cause) {
    super(message, cause);
  }

  @Builder
  public static ListenerException buildException(
      String message,
      AppError appError,
      @Singular List<String> errorArguments,
      Exception exception,
      @Singular Map<String, String> invalidAttributes,
      @Singular Map<String, Object> extraParams) {

    String exceptionMessage = StringUtils.hasText(message) ? message : "";

    if (appError != null) {
      exceptionMessage = appError.toLocale(errorArguments.toArray(new String[]{}));
    } else if (exception != null) {
      exceptionMessage = exception.getMessage();
    }

    ListenerException listenerException;

    if (ObjectUtils.isEmpty(exception)) {
      listenerException = new ListenerException(exceptionMessage);
    } else {
      listenerException = new ListenerException(exceptionMessage, exception);
    }
    Optional.ofNullable(appError).ifPresent(listenerException::setError);
    Optional.ofNullable(invalidAttributes).ifPresent(listenerException::setInvalidAttributes);
    Optional.ofNullable(extraParams).ifPresent(listenerException::setExtraParams);

    return listenerException;
  }
}
