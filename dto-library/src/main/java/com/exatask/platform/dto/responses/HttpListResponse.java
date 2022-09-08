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
public class HttpListResponse<T> extends AppResponse {

  private Long count;

  private List<T> data;

  public HttpListResponse(Long count, List<T> data) {
    this(count, data, null);
  }

  public HttpListResponse(Long count, List<T> data, List<Link> links) {

    this.count = count;
    this.data = data;

    if (!ObjectUtils.isEmpty(links)) {
      this.add(links);
    }
  }
}
