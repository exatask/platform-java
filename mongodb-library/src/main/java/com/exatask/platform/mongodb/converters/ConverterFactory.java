package com.exatask.platform.mongodb.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ConverterFactory {

  private static final Map<Class<?>, Converter<?, ?>> converterList = new HashMap<>();

  @Autowired
  public ConverterFactory(Set<Converter<?, ?>> converters) {
    createConverterList(converters);
  }

  private void createConverterList(Set<Converter<?, ?>> converters) {
    for (Converter<?, ?> converter : converters) {
      converterList.put(converter.getAnnotation(), converter);
    }
  }

  public static Converter<?, ?> getConverter(Class<?> annotationClass) {

    if (!converterList.containsKey(annotationClass)) {
      throw new IllegalArgumentException();
    }

    return converterList.get(annotationClass);
  }

  public static Map<Class<?>, Converter<?, ?>> getConverters() {
    return converterList;
  }

  public static Set<Class<?>> getConverterAnnotations() {
    return converterList.keySet();
  }
}
