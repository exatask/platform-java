package com.exatask.platform.storage.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MetadataProperties {

  ORIGINAL_FILENAME("x-original-filename", "x-amz-meta-original-filename"),
  DOWNLOAD_FILENAME("x-download-filename", "x-amz-meta-download-filename");

  private final String sftpKey;
  private final String awsKey;
}
