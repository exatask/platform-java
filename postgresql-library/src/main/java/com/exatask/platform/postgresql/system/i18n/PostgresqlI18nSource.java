package com.exatask.platform.postgresql.system.i18n;

import com.exatask.platform.i18n.sources.AppI18nSource;
import org.springframework.stereotype.Service;

@Service
public class PostgresqlI18nSource implements AppI18nSource {

  @Override
  public String[] getMessageSources() {
    return new String[] {"i18n.postgresql-validations"};
  }
}
