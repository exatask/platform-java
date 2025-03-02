package com.exatask.platform.storage.transports;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppTransportType {

  SFTP("file://", "file-", ""),
  AWS("aws://", "aws-", ""),
  GCP("gcp://", "gcp-", "");

  private final String pathPrefix;

  private final String filePrefix;

  private final String fileSuffix;
}
