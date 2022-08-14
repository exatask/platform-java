package com.exatask.platform.mongodb.tenants;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;

public interface ServiceTenant {

  String getServiceKey();

  MongoProperties getMongoProperties();
}
