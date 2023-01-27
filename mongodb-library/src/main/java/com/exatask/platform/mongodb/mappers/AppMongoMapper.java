package com.exatask.platform.mongodb.mappers;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class AppMongoMapper {

  public String objectIdToString(ObjectId objectId) {

    if (ObjectUtils.isEmpty(objectId)) {
      return null;
    }

    return objectId.toHexString();
  }
}
