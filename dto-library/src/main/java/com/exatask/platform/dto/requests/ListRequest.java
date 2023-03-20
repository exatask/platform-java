package com.exatask.platform.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ListRequest extends AppRequest {

  @Min(1)
  private Integer page = 1;

  @Min(1)
  @Max(100)
  private Integer limit = 25;

  private List<String> sort;

  private List<String> fields;

  public Map<String, Integer> parseSort() {

    Map<String, Integer> sortList = new HashMap<>();
    if (ObjectUtils.isEmpty(sort)) {
      return sortList;
    }

    for (String field : sort) {
      if (field.charAt(0) == HttpRequestOperators.NEGATION_OPERATOR) {
        sortList.put(field.substring(1), -1);
      } else {
        sortList.put(field, 1);
      }
    }

    return sortList;
  }

  public Integer getSkip() {
    return (page - 1) * limit;
  }
}
