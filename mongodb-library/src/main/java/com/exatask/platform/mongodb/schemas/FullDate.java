package com.exatask.platform.mongodb.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = FullDate.FullDateBuilder.class)
public class FullDate {

  @JsonProperty("date")
  @Field("date")
  private Integer date;

  @JsonProperty("month")
  @Field("month")
  private Integer month;

  @JsonProperty("year")
  @Field("year")
  private Integer year;
}
