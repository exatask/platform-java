package com.exatask.platform.service.responses;

import com.exatask.platform.service.responses.messages.AppResponseMessage;
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
public class HttpGenericResponse<T> extends HttpSuccessResponse {

  private T data;

  public HttpGenericResponse(T data, AppResponseMessage responseMessage) {

    super(responseMessage);
    this.data = data;
  }
}
