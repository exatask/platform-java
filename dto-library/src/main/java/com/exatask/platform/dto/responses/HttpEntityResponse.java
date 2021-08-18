package com.exatask.platform.dto.responses;

import com.exatask.platform.dto.responses.messages.AppResponseMessage;
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
public class HttpEntityResponse<T> extends HttpSuccessResponse {

  private T id;

  public HttpEntityResponse(T id, AppResponseMessage responseMessage) {

    super(responseMessage);
    this.id = id;
  }
}
