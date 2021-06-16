package com.exatask.platform.mongodb.converters;

import com.exatask.platform.mongodb.annotations.Precision;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PrecisionConverter implements AppConverter<Double, Double> {

  @Override
  public Class<?> getAnnotation() {
    return Precision.class;
  }

  private Double convert(Object data, Annotation annotation, Field field) {

    Precision precision = (Precision) annotation;
    if (precision.value() < 0) {
      return Double.NEGATIVE_INFINITY;
    }

    BigDecimal bigDecimalValue = BigDecimal.valueOf(Double.parseDouble(data.toString()));
    bigDecimalValue = bigDecimalValue.setScale(precision.value(), RoundingMode.HALF_UP);
    return bigDecimalValue.doubleValue();
  }

  @Override
  public Double write(Object data, Annotation annotation, Field field) {
    return convert(data, annotation, field);
  }

  @Override
  public Double read(Object data, Annotation annotation, Field field) {
    return convert(data, annotation, field);
  }
}
