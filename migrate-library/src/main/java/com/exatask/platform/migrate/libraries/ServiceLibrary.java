package com.exatask.platform.migrate.libraries;

import com.exatask.platform.crypto.authenticators.AppAuthenticator;
import com.exatask.platform.crypto.authenticators.AppAuthenticatorFactory;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.properties.FeignProperties;
import com.exatask.platform.utilities.services.ServiceAuthData;
import org.springframework.web.reactive.function.client.WebClient;

public class ServiceLibrary {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected WebClient webClient;

  protected void initWebClient(FeignProperties feignProperties) {

    AppAuthenticator authenticator = AppAuthenticatorFactory.getAuthenticator(feignProperties.credentials());
    this.webClient = WebClient.builder()
        .baseUrl(feignProperties.httpHost())
        .defaultHeader(ServiceAuthData.AUTH_HEADER, authenticator.getAuthentication().getPrefix() + " " + authenticator.generate())
        .build();
  }
}