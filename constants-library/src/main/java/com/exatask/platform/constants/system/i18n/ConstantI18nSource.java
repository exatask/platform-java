package com.exatask.platform.constants.system.i18n;

import com.exatask.platform.i18n.sources.AppI18nSource;
import org.springframework.stereotype.Service;

@Service
public class ConstantI18nSource implements AppI18nSource {

  @Override
  public String[] getMessageSources() {
    return new String[] {"i18n.entity-constants"};
  }
}
