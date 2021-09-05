package com.exatask.platform.storage.transports;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppTransportType {

  SFTP("file://"),
  AWS("aws://");

  private final String pathPrefix;
}
