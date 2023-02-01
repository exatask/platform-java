package com.exatask.platform.dao.migration.libraries;

import com.exatask.platform.dao.libraries.AppLibrary;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.web.reactive.function.client.WebClient;

public class ServiceLibrary extends AppLibrary {

  public WebClient createWebClient(String authPrefix, String authToken) {

    return WebClient.builder()
        .baseUrl("http://localhost:" + ServiceUtility.getServiceProperty("server.port"))
        .defaultHeader(ServiceAuthData.AUTH_HEADER, authPrefix + " " + authToken)
        .build();
  }
}
