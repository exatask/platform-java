package com.exatask.platform.service.responses;

import com.exatask.platform.service.responses.messages.ResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpErrorResponse extends AppResponse {

  private final ResponseMessage message;

  private final Map<String, String> invalidAttributes;

  private final Map<String, Object> extraParams;

  private final StackTraceElement[] stackTrace;

  private final String exceptionCause;
}
