package com.exatask.platform.service.responses;

import com.exatask.platform.service.responses.messages.AppResponseMessage;
import com.exatask.platform.service.responses.messages.ResponseMessage;
import com.exatask.platform.service.responses.messages.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpSuccessResponse extends AppResponse {

  private final ResponseMessage message;

  public HttpSuccessResponse(AppResponseMessage responseMessage) {

    message = ResponseMessage.builder()
        .type(MessageType.SUCCESS)
        .text(responseMessage.toLocale())
        .build();
  }
}
