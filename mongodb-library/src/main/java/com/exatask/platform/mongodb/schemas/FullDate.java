package com.exatask.platform.mongodb.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Jacksonized
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullDate {

  @Field("date")
  private Integer date;

  @Field("month")
  private Integer month;

  @Field("year")
  private Integer year;
}
