package com.exatask.platform.api.changelogs.libraries;

import com.exatask.platform.api.libraries.AppLibrary;
import com.exatask.platform.crypto.authenticators.AppAuthenticator;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.web.reactive.function.client.WebClient;

public class ServiceLibrary extends AppLibrary {

  protected WebClient webClient;

  protected ServiceLibrary(AppAuthenticator authenticator) {

    this.webClient = WebClient.builder()
        .baseUrl("http://localhost:" + ServiceUtility.getServiceProperty("server.port"))
        .defaultHeader(ServiceAuthData.AUTH_HEADER, authenticator.getAuthentication().getPrefix() + " " + authenticator.generate())
        .build();
  }
}
