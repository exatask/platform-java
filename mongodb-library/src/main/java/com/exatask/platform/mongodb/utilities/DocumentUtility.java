package com.exatask.platform.mongodb.utilities;

import com.exatask.platform.mongodb.converters.AppConverterFactory;
import com.exatask.platform.mongodb.fields.FieldAnnotations;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.mongodb.core.mapping.Field;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class DocumentUtility {

  private static final Map<Class<?>, Map<String, FieldAnnotations>> converterAnnotations = new HashMap<>();

  private static Map<String, FieldAnnotations> processConverterAnnotation(Class<?> clazz) {

    java.lang.reflect.Field[] fields = FieldUtils.getAllFields(clazz);
    Map<String, FieldAnnotations> fieldAnnotationList = new HashMap<>();
    Set<Class<?>> converters = AppConverterFactory.getConverterAnnotations();

    for (java.lang.reflect.Field field : fields) {

      Field annotatedField = field.getAnnotation(Field.class);

      if (field.getType().isMemberClass() && !field.getType().isEnum()) {

        Map<String, FieldAnnotations> nestedConverterAnnotations = processConverterAnnotation(field.getType());
        if (!nestedConverterAnnotations.isEmpty()) {

          FieldAnnotations fieldInfo = FieldAnnotations.builder().field(field).nestedFields(nestedConverterAnnotations).build();
          fieldAnnotationList.put(annotatedField.value(), fieldInfo);
        }

      } else {

        List<Annotation> converterAnnotationList = new ArrayList<>();
        Annotation[] fieldAnnotations = field.getDeclaredAnnotations();

        for (Annotation annotation : fieldAnnotations) {
          if (converters.contains(annotation.annotationType())) {
            converterAnnotationList.add(annotation);
          }
        }

        if (converterAnnotationList.size() > 0) {

          FieldAnnotations fieldInfo = FieldAnnotations.builder().field(field).annotations(converterAnnotationList).build();
          fieldAnnotationList.put(annotatedField.value(), fieldInfo);
        }
      }
    }

    return fieldAnnotationList;
  }

  public static Map<String, FieldAnnotations> getConverterAnnotations(Class<?> clazz) {

    if (converterAnnotations.containsKey(clazz)) {
      return converterAnnotations.get(clazz);
    }

    Map<String, FieldAnnotations> convertedAnnotations = processConverterAnnotation(clazz);
    converterAnnotations.put(clazz, convertedAnnotations);
    return convertedAnnotations;
  }
}
