package com.exatask.platform.dao.migration.libraries;

import com.exatask.platform.dao.libraries.AppLibrary;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.web.reactive.function.client.WebClient;

public class ServiceLibrary extends AppLibrary {

  public WebClient createWebClient(String authType, String authToken) {

    return WebClient.builder()
        .baseUrl("http://localhost:" + ServiceUtility.getServiceProperty("server.port"))
        .defaultHeader(ServiceAuthData.AUTH_TYPE_HEADER, authType)
        .defaultHeader(ServiceAuthData.AUTH_TOKEN_HEADER, authToken)
        .build();
  }
}
