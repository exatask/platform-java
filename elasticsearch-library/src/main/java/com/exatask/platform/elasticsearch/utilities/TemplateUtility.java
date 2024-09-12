package com.exatask.platform.elasticsearch.utilities;

import com.exatask.platform.utilities.properties.ElasticsearchProperties;
import lombok.experimental.UtilityClass;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@UtilityClass
public class TemplateUtility {

  public static ElasticsearchRestTemplate getTemplate(RestHighLevelClient restClient) {
    return new ElasticsearchRestTemplate(restClient);
  }

  public static RestHighLevelClient getClient(ElasticsearchProperties elasticProperties) {

    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo(elasticProperties.getUris().toArray(new String[]{}))
        .withBasicAuth(elasticProperties.getUsername(), elasticProperties.getPassword())
        .withConnectTimeout(elasticProperties.getConnectionTimeout())
        .build();

    return RestClients.create(clientConfiguration).rest();
  }
}
