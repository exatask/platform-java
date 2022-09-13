package com.exatask.platform.mongodb.mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class AppMongoMapper {

  public String objectIdToString(ObjectId objectId) {
    return objectId.toHexString();
  }
}
