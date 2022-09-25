package com.exatask.platform.mongodb.fields;

import lombok.Builder;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class FieldAnnotations {

  private final List<Annotation> annotations;

  private final Map<String, FieldAnnotations> nestedFields;
}
