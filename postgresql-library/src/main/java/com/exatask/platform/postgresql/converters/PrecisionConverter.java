package com.exatask.platform.postgresql.converters;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class PrecisionConverter implements AttributeConverter<Double, Double> {

  public abstract int precision();

  private Double convert(Double data) {

    if (precision() < 0) {
      return Double.NEGATIVE_INFINITY;
    }

    BigDecimal bigDecimalValue = BigDecimal.valueOf(data);
    bigDecimalValue = bigDecimalValue.setScale(precision(), RoundingMode.HALF_UP);
    return bigDecimalValue.doubleValue();
  }

  @Override
  public Double convertToDatabaseColumn(Double data) {
    return convert(data);
  }

  @Override
  public Double convertToEntityAttribute(Double data) {
    return convert(data);
  }
}
