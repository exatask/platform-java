package com.exatask.platform.api.responses;

import com.exatask.platform.api.constants.ResponseMessage;
import com.exatask.platform.api.responses.messages.AppResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpEntityResponse<T> extends AppResponse {

  private final T id;

  private final Message message;

  public HttpEntityResponse(T id, AppResponseMessage responseMessage) {

    this.id = id;
    this.message = new Message(ResponseMessage.SUCCESS, responseMessage.toLocale());
  }

  @Getter
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class Message {

    private final ResponseMessage type;

    private final String text;
  }
}
