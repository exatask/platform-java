package com.exatask.platform.elasticsearch.utilities;

import com.exatask.platform.utilities.properties.ElasticsearchProperties;
import lombok.experimental.UtilityClass;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;

@UtilityClass
public class ReactiveTemplateUtility {

  public static ReactiveElasticsearchTemplate getTemplate(ReactiveElasticsearchClient elasticsearchClient) {
    return new ReactiveElasticsearchTemplate(elasticsearchClient);
  }

  public static ReactiveElasticsearchClient getClient(ElasticsearchProperties elasticProperties) {

    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo(elasticProperties.getUris().toArray(new String[]{}))
        .withBasicAuth(elasticProperties.getUsername(), elasticProperties.getPassword())
        .withConnectTimeout(elasticProperties.getConnectionTimeout())
        .build();

    return ReactiveRestClients.create(clientConfiguration);
  }
}
