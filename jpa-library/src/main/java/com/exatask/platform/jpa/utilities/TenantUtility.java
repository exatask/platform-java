package com.exatask.platform.jpa.utilities;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class TenantUtility {

  public static String getTenantKey(String serviceKey, String tenant) {
    return serviceKey + "_" + StringUtils.defaultIfEmpty(tenant, "default");
  }
}
