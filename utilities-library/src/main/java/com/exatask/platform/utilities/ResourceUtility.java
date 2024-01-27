package com.exatask.platform.utilities;

import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.exatask.platform.utilities.properties.UrnProperties;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ResourceUtility {

  private static final String URN_PREFIX = "ern";
  private static final String URN_SEPARATOR = ":";
  private static final String RESOURCE_SEPARATOR = "/";

  public String urn(UrnProperties urnProperties) {
    return urn(urnProperties.getService(), urnProperties.getTenant(), urnProperties.getAccountNumber(), urnProperties.getResource(), urnProperties.getResourceId());
  }

  public String urn(String serviceCode, String resource, String resourceId) {
    return urn(serviceCode, ServiceUtility.getServiceTenant(), RequestContextProvider.getAccountNumber(), resource, resourceId);
  }

  public String urn(String serviceCode, Long accountNumber, String resource, String resourceId) {
    return urn(serviceCode, ServiceUtility.getServiceTenant(), accountNumber, resource, resourceId);
  }

  public String urn(String service, String tenant, Long accountNumber, String resource, String resourceId) {

    String resourceUrn = URN_PREFIX + URN_SEPARATOR + service + URN_SEPARATOR;
    if (StringUtils.isNotEmpty(tenant)) {
      resourceUrn += tenant;
    }

    resourceUrn += URN_SEPARATOR;
    if (accountNumber != null && accountNumber > 0) {
      resourceUrn += String.valueOf(accountNumber);
    }

    resourceUrn += URN_SEPARATOR + resource + RESOURCE_SEPARATOR + resourceId;
    return resourceUrn;
  }

  public UrnProperties parseUrn(String urn) {

    String[] resourceElements = urn.split(RESOURCE_SEPARATOR);
    String[] urnElements = resourceElements[0].split(URN_SEPARATOR);
    UrnProperties.UrnPropertiesBuilder urnPropertiesBuilder = UrnProperties.builder();

    if (!urnElements[0].equals(URN_PREFIX)) {
      return null;
    }

    urnPropertiesBuilder.service(urnElements[1])
        .tenant(urnElements[2]);

    if (StringUtils.isNotEmpty(urnElements[3])) {
        urnPropertiesBuilder.accountNumber(Long.parseLong(urnElements[3]));
    }

    urnPropertiesBuilder.resource(urnElements[4])
        .resourceId(resourceElements[1]);

    return urnPropertiesBuilder.build();
  }
}
