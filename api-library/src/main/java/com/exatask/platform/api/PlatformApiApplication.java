package com.exatask.platform.api;

import com.exatask.platform.api.utilities.ApiSwaggerUtility;
import com.exatask.platform.i18n.utilities.I18nSource;

public interface PlatformApiApplication {

  ApiSwaggerUtility getSwaggerUtility();

  I18nSource getI18nMessageSource();
}
