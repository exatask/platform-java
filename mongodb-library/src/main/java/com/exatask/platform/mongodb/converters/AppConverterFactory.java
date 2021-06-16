package com.exatask.platform.mongodb.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AppConverterFactory {

  private static final Map<Class<?>, AppConverter<?, ?>> converterList = new HashMap<>();

  @Autowired
  public AppConverterFactory(Set<AppConverter<?, ?>> converters) {
    createConverterList(converters);
  }

  private void createConverterList(Set<AppConverter<?, ?>> converters) {
    for (AppConverter<?, ?> converter : converters) {
      converterList.put(converter.getAnnotation(), converter);
    }
  }

  public static AppConverter<?, ?> getConverter(Class<?> annotationClass) {

    if (!converterList.containsKey(annotationClass)) {
      throw new IllegalArgumentException();
    }

    return converterList.get(annotationClass);
  }

  public static Map<Class<?>, AppConverter<?, ?>> getConverters() {
    return converterList;
  }

  public static Set<Class<?>> getConverterAnnotations() {
    return converterList.keySet();
  }
}
