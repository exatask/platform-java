package com.exatask.platform.mongodb.converters;

import com.exatask.platform.mongodb.converters.annotations.Precision;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PrecisionConverter implements AppConverter<Double, Double> {

  @Override
  public Class<?> getAnnotation() {
    return Precision.class;
  }

  private Double convert(Object data, Annotation annotation) {

    Precision precision = (Precision) annotation;
    if (precision.value() < 0) {
      return Double.NEGATIVE_INFINITY;
    }

    BigDecimal bigDecimalValue = BigDecimal.valueOf(Double.parseDouble(data.toString()));
    bigDecimalValue = bigDecimalValue.setScale(precision.value(), RoundingMode.HALF_UP);
    return bigDecimalValue.doubleValue();
  }

  @Override
  public Double convertToDatabaseColumn(Object data, Annotation annotation) {
    return convert(data, annotation);
  }

  @Override
  public Double convertToEntityAttribute(Object data, Annotation annotation) {
    return convert(data, annotation);
  }
}
