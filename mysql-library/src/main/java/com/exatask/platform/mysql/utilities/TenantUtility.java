package com.exatask.platform.mysql.utilities;

import com.exatask.platform.utilities.constants.Tenant;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class TenantUtility {

  public static String getTenantKey(String serviceKey, String tenant) {
    return serviceKey + "_" + StringUtils.defaultIfEmpty(tenant, Tenant.DEFAULT_TENANT);
  }
}
