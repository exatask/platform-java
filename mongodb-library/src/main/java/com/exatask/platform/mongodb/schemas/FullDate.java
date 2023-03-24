package com.exatask.platform.mongodb.schemas;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class FullDate {

  @Min(value = 1, message = "{validations.full-date.date.min}")
  @Max(value = 31, message = "{validations.full-date.date.max}")
  @NotNull(message = "{validations.full-date.date.not-null}")
  @Field("date")
  private Integer date;

  @Min(value = 1, message = "{validations.full-date.month.min}")
  @Max(value = 12, message = "{validations.full-date.month.max}")
  @NotNull(message = "{validations.full-date.month.not-null}")
  @Field("month")
  private Integer month;

  @Min(value = 1900, message = "{validations.full-date.year.min}")
  @NotNull(message = "{validations.full-date.year.not-null}")
  @Field("year")
  private Integer year;
}
