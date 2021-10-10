package com.exatask.platform.storage.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MetadataProperties {

  ORIGINAL_FILENAME("x-original-filename"),
  DOWNLOAD_FILENAME("x-download-filename");

  private final String key;
}
