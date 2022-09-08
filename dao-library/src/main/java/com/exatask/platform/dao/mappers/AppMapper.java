package com.exatask.platform.dao.mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppMapper {

  public String objectIdToString(ObjectId objectId) {
    return objectId.toHexString();
  }

  public String uuidToString(UUID uuid) {
    return uuid.toString();
  }
}
