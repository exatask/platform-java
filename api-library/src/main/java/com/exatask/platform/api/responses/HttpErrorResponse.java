package com.exatask.platform.api.responses;

import com.exatask.platform.api.constants.Environment;
import com.exatask.platform.api.exceptions.HttpException;
import com.exatask.platform.api.utilities.ApiServiceUtility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpErrorResponse extends AppResponse {

  @JsonProperty("message")
  private final Message message;

  @JsonProperty("invalid_attributes")
  private Map<String, String> invalidAttributes;

  @JsonProperty("extra_params")
  private Map<String, Object> extraParams;

  @JsonProperty("stack_trace")
  private StackTraceElement[] stackTrace;

  public HttpErrorResponse(HttpException exception) {

    status = false;
    message = new Message("error", exception.getMessage(), exception.getError().getErrorCode());
    invalidAttributes = exception.getInvalidAttributes();
    extraParams = exception.getExtraParams();

    if (ApiServiceUtility.getServiceEnvironment() != Environment.RELEASE) {
      stackTrace = exception.getStackTrace();
    }
  }

  public HttpErrorResponse(Exception exception) {

    status = false;
    message = new Message("error", exception.getMessage(), null);

    if (ApiServiceUtility.getServiceEnvironment() != Environment.RELEASE) {
      stackTrace = exception.getStackTrace();
    }
  }

  @Getter
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Message {

    @JsonProperty("type")
    private final String type;

    @JsonProperty("text")
    private final String text;

    @JsonProperty("error_code")
    private final String errorCode;
  }
}
