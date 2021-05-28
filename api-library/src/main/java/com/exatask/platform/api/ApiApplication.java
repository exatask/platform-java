package com.exatask.platform.api;

import com.exatask.platform.api.authenticators.Authenticator;
import com.exatask.platform.api.configurations.ApiSwaggerConfig;
import com.exatask.platform.i18n.utilities.I18nSource;

public interface ApiApplication {

  ApiSwaggerConfig getSwaggerConfig();

  I18nSource getI18nMessageSource();

  Authenticator getApiAuthenticator();
}
