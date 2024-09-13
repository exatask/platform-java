package com.exatask.platform.elasticsearch.libraries;

import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ElasticsearchLibrary {

  public void createIndex(IndexOperations indexOperations, Map<String, String> fields) {
    createIndex(indexOperations, fields, 1, 1);
  }

  public void createIndex(IndexOperations indexOperations, Map<String, String> fields, int shards, int replicas) {

    Map<String, Object> settings = new HashMap<>();
    settings.put("index.number_of_shards", shards);
    settings.put("index.number_of_replicas", replicas);

    Map<String, Object> properties = new HashMap<>();
    for (Map.Entry<String, String> field : fields.entrySet()) {
      properties.put(field.getKey(), Collections.singletonMap("type", field.getValue()));
    }

    Map<String, Object> source = new HashMap<>();
    source.put("settings", settings);
    source.put("mappings", Collections.singletonMap("properties", properties));

    indexOperations.putMapping(Document.from(source));
  }
}
