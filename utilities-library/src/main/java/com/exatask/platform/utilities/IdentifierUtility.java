package com.exatask.platform.utilities;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class IdentifierUtility {

  public boolean isUuid(String uuid) {

    try {
      UUID.fromString(uuid);
    } catch (IllegalArgumentException exception) {
      return false;
    }

    return true;
  }
}
