package com.exatask.platform.elasticsearch.utilities;

import lombok.experimental.UtilityClass;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@UtilityClass
public class TemplateUtility {

  public static ElasticsearchRestTemplate getTemplate(RestHighLevelClient restClient) {
    return new ElasticsearchRestTemplate(restClient);
  }

  public static RestHighLevelClient getClient(ElasticsearchRestClientProperties elasticProperties) {

    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo(elasticProperties.getUris().toArray(new String[]{}))
        .withBasicAuth(elasticProperties.getUsername(), elasticProperties.getPassword())
        .build();

    return RestClients.create(clientConfiguration).rest();
  }
}
