package com.exatask.platform.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpDetailResponse<T> extends AppResponse {

  private T data;

  public HttpDetailResponse(T data) {
    this(data, null);
  }

  public HttpDetailResponse(T data, List<Link> links) {

    this.data = data;

    if (!ObjectUtils.isEmpty(links)) {
      this.add(links);
    }
  }
}
