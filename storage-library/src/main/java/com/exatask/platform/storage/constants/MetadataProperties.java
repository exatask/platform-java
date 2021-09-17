package com.exatask.platform.storage.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MetadataProperties {

  ORIGINAL_FILENAME("x-original-filename");

  private final String key;
}
