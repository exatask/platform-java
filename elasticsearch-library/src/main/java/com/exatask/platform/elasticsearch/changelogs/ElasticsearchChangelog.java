package com.exatask.platform.elasticsearch.changelogs;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@RequiredArgsConstructor
public abstract class ElasticsearchChangelog {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected final ElasticsearchRestTemplate elasticsearchTemplate;

  public abstract void execute();
}
