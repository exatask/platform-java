package com.exatask.platform.elasticsearch.healthcheck;

import com.exatask.platform.elasticsearch.constants.ElasticsearchService;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Component
public class ElasticsearchHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<ElasticsearchRestTemplate> elasticTemplates;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> elasticHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(elasticTemplates)) {
      return elasticHealthCheckData;
    }

    for (ElasticsearchRestTemplate template : elasticTemplates) {

      MainResponse mainResponse = template.execute(client -> client.info(RequestOptions.DEFAULT));
      ClusterHealthResponse clusterResponse = template.execute(client -> client.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT));

      elasticHealthCheckData.add(ServiceHealthCheckData.builder()
          .status(clusterResponse.getStatus() != ClusterHealthStatus.RED)
          .version(mainResponse.getVersion().getNumber())
          .build());
    }

    return elasticHealthCheckData;
  }

  @Override
  public String getName() {
    return ElasticsearchService.HEALTH_CHECK_NAME;
  }
}
