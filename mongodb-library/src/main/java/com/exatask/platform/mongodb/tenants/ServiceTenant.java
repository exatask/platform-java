package com.exatask.platform.mongodb.tenants;

import com.exatask.platform.utilities.properties.MongodbProperties;

public interface ServiceTenant {

  String getServiceKey();

  MongodbProperties getMongoProperties();
}
