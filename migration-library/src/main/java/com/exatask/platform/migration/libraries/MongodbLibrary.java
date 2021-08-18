package com.exatask.platform.migration.libraries;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

import java.util.List;

public class MongodbLibrary extends AppLibrary {

  public void createIndex(MongoCollection collection, List<String> fields, String name) {
    createIndex(collection, fields, name, false, false);
  }

  public void createIndex(MongoCollection collection, List<String> fields, String name, boolean unique) {
    createIndex(collection, fields, name, unique, false);
  }

  public void createIndex(MongoCollection collection, List<String> fields, String name, boolean unique, boolean sparse) {

    BsonDocument index = new BsonDocument();
    fields.forEach(field -> index.append(field, new BsonInt32(1)));

    IndexOptions indexOptions = new IndexOptions();
    indexOptions.background(true).unique(unique).sparse(sparse).name(name);
    collection.createIndex(index, indexOptions);
  }
}
