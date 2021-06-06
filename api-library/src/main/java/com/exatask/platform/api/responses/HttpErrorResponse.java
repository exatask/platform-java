package com.exatask.platform.api.responses;

import com.exatask.platform.api.constants.ResponseMessage;
import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.utilities.ApiServiceUtility;
import com.exatask.platform.utilities.constants.Environment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpErrorResponse extends AppResponse {

  private final Message message;

  private Map<String, String> invalidAttributes;

  private Map<String, Object> extraParams;

  private StackTraceElement[] stackTrace;

  @JsonIgnore
  private Throwable exceptionCause;

  public HttpErrorResponse(HttpException exception) {

    status = false;
    message = new Message(ResponseMessage.ERROR, exception.getMessage(), exception.getError().getErrorCode());
    invalidAttributes = exception.getInvalidAttributes();
    extraParams = exception.getExtraParams();

    if (ApiServiceUtility.getServiceEnvironment() != Environment.RELEASE) {
      stackTrace = exception.getStackTrace();
      exceptionCause = exception.getCause();
    }
  }

  public HttpErrorResponse(Exception exception) {

    status = false;
    message = new Message(ResponseMessage.ERROR, exception.getMessage(), null);

    if (ApiServiceUtility.getServiceEnvironment() != Environment.RELEASE) {
      stackTrace = exception.getStackTrace();
      exceptionCause = exception.getCause();
    }
  }

  @JsonProperty("exception_cause")
  public String getExceptionCause() {
    return ObjectUtils.defaultIfNull(exceptionCause, "").toString();
  }

  @Getter
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class Message {

    private final ResponseMessage type;

    private final String text;

    private final String errorCode;
  }
}
