package com.exatask.platform.api.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

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
  @Parameter(in = ParameterIn.QUERY)
  private Integer page = 1;

  @Min(1)
  @Max(100)
  @Parameter(in = ParameterIn.QUERY, schema = @Schema(defaultValue = "25", maximum = "100"))
  private Integer limit = 25;

  @Parameter(in = ParameterIn.QUERY)
  private List<String> sort;

  @Parameter(in = ParameterIn.QUERY)
  private List<String> fields;

  @Hidden
  public Map<String, Integer> parseSort() {

    Map<String, Integer> sortList = new HashMap<>();
    if (ObjectUtils.isEmpty(sort)) {
      return sortList;
    }

    for (String field : sort) {
      if (field.charAt(0) == NEGATION_OPERATOR) {
        sortList.put(field.substring(1), -1);
      } else {
        sortList.put(field, 1);
      }
    }

    return sortList;
  }

  @Hidden
  public Integer getSkip() {
    return (page - 1) * limit;
  }
}
