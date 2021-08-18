package com.exatask.platform.dto.responses;

import com.exatask.platform.dto.responses.messages.AppResponseMessage;
import com.exatask.platform.dto.responses.messages.MessageType;
import com.exatask.platform.dto.responses.messages.ResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpSuccessResponse extends AppResponse {

  private ResponseMessage message;

  public HttpSuccessResponse(AppResponseMessage responseMessage) {

    message = new ResponseMessage();
    message.setType(MessageType.SUCCESS)
        .setText(responseMessage.toLocale());
  }
}
