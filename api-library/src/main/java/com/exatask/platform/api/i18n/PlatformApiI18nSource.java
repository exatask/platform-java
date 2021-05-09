package com.exatask.platform.api.i18n;

import com.exatask.platform.i18n.utilities.I18nSource;
import org.springframework.stereotype.Service;

@Service
public class PlatformApiI18nSource implements I18nSource {

  @Override
  public String[] getMessageSources() {
    return new String[] {"i18n.api-constants"};
  }
}
