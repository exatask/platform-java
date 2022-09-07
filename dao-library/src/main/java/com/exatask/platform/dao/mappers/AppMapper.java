package com.exatask.platform.dao.mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class AppMapper {

  public String objectIdToString(ObjectId objectId) {
    return objectId.toHexString();
  }
}
