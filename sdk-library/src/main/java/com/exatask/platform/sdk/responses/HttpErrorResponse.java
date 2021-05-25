package com.exatask.platform.sdk.responses;

import com.exatask.platform.sdk.constants.ResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpErrorResponse extends AppResponse {

  private Message message;

  private Map<String, String> invalidAttributes;

  private Map<String, Object> extraParams;

  private StackTraceElement[] stackTrace;

  private Throwable exceptionCause;

  @Getter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class Message {

    private ResponseMessage type;

    private String text;

    private String errorCode;
  }
}
