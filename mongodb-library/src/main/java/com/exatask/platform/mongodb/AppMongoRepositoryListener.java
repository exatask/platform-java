package com.exatask.platform.mongodb;

import com.exatask.platform.mongodb.converters.AppConverter;
import com.exatask.platform.mongodb.converters.AppConverterFactory;
import com.exatask.platform.mongodb.constants.Operation;
import com.exatask.platform.mongodb.fields.FieldAnnotations;
import com.exatask.platform.mongodb.utilities.DocumentUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Service
public class AppMongoRepositoryListener extends AbstractMongoEventListener<AppModel> {

  private void convertFields(Document model, Map<String, FieldAnnotations> converterAnnotations, Operation operation) {

    for (Map.Entry<String, Object> modelSet : model.entrySet()) {

      if (!converterAnnotations.containsKey(modelSet.getKey())) {
        continue;
      }

      FieldAnnotations fieldInfo = converterAnnotations.get(modelSet.getKey());
      Map<String, FieldAnnotations> nestedFields = fieldInfo.getNestedFields();
      List<Annotation> fieldAnnotations = fieldInfo.getAnnotations();
      Object value = modelSet.getValue();

      if (nestedFields != null && value instanceof Document) {
        convertFields((Document) value, nestedFields, operation);
      } else if (fieldAnnotations != null) {

        for (Annotation annotation : fieldAnnotations) {

          AppConverter<?, ?> converter = AppConverterFactory.getConverter(annotation.annotationType());
          if (operation == Operation.READ) {
            value = converter.read(value, annotation, fieldInfo.getField());
          } else {
            value = converter.write(value, annotation, fieldInfo.getField());
          }
        }

        model.put(modelSet.getKey(), value);
      }
    }
  }

  @Override
  public void onBeforeSave(BeforeSaveEvent<AppModel> event) {

    Document model = event.getDocument();
    Map<String, FieldAnnotations> converterAnnotations = DocumentUtility.getConverterAnnotations(event.getSource().getClass());
    if (!ObjectUtils.isEmpty(model) && !CollectionUtils.isEmpty(converterAnnotations)) {
      convertFields(model, converterAnnotations, Operation.WRITE);
    }
  }

  @Override
  public void onAfterLoad(AfterLoadEvent<AppModel> event) {

    Document model = event.getSource();
    Map<String, FieldAnnotations> converterAnnotations = DocumentUtility.getConverterAnnotations(event.getType());
    if (!CollectionUtils.isEmpty(converterAnnotations)) {
      convertFields(model, converterAnnotations, Operation.READ);
    }
  }
}
