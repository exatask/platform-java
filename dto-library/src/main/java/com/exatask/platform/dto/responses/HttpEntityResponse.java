package com.exatask.platform.dto.responses;

import com.exatask.platform.dto.responses.messages.AppResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpEntityResponse<T> extends HttpSuccessResponse {

  private T data;

  public HttpEntityResponse(T data, AppResponseMessage responseMessage) {
    this(data, responseMessage, null);
  }

  public HttpEntityResponse(T data, AppResponseMessage responseMessage, List<Link> links) {

    super(responseMessage, links);
    this.data = data;
  }
}
