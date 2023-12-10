package com.exatask.platform.utilities;

import com.exatask.platform.utilities.properties.UrnProperties;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ResourceUtility {

  private static final String URN_PREFIX = "ern";
  private static final String URN_SEPARATOR = ":";
  private static final String RESOURCE_SEPARATOR = "/";

  private static final int RESOURCE_ID_LENGTH = 8;

  public String urn(UrnProperties urnProperties) {
    return urn(urnProperties.getService(), urnProperties.getTenant(), urnProperties.getAccountNumber(), urnProperties.getResource(), urnProperties.getResourceId());
  }

  public String urn(String serviceCode, String accountNumber, String resource) {
    return urn(serviceCode, ServiceUtility.getServiceTenant(), accountNumber, resource, RandomUtility.alphaNumeric(RESOURCE_ID_LENGTH));
  }

  public String urn(String serviceCode, String accountNumber, String resource, String resourceId) {
    return urn(serviceCode, ServiceUtility.getServiceTenant(), accountNumber, resource, resourceId);
  }

  public String urn(String service, String tenant, String accountNumber, String resource, String resourceId) {

    String resourceUrn = URN_PREFIX + URN_SEPARATOR + service + URN_SEPARATOR;
    if (StringUtils.isNotEmpty(tenant)) {
      resourceUrn += tenant;
    }

    resourceUrn += URN_SEPARATOR;
    if (StringUtils.isNotEmpty(accountNumber)) {
      resourceUrn += accountNumber;
    }

    resourceUrn += URN_SEPARATOR + resource + RESOURCE_SEPARATOR + resourceId;
    return resourceUrn;
  }

  public UrnProperties parseUrn(String urn) {

    String[] urnElements = urn.split(URN_SEPARATOR);
    UrnProperties.UrnPropertiesBuilder urnPropertiesBuilder = UrnProperties.builder();

    if (!urnElements[0].equals(URN_PREFIX)) {
      return null;
    }

    urnPropertiesBuilder.service(urnElements[1])
        .tenant(urnElements[2])
        .accountNumber(urnElements[3]);

    String[] resourceElements = urnElements[4].split(RESOURCE_SEPARATOR);
    urnPropertiesBuilder.resource(resourceElements[0])
        .resourceId(resourceElements[1]);

    return urnPropertiesBuilder.build();
  }
}
