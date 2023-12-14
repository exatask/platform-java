package com.exatask.platform.dao.configurations;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DaoServiceConfig {

  private String code;

  private String uri;

  private String environment;
}
