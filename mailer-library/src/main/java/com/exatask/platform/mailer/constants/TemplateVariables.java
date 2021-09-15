package com.exatask.platform.mailer.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TemplateVariables {

  private static final String TEMPLATE_PREFIX = "template_";

  public static final String TEMPLATE_SEPARATOR = "_";
  public static final String PROPERTY_SEPARATOR = ".";

  public static final String TIMESTAMP_YEAR = "timestamp_year";

  public static final String APPLICATION_NAME = TEMPLATE_PREFIX + "application_name";

  public static final String CONTACT_EMAIL_ID = TEMPLATE_PREFIX + "contact_emailId";

  public static final String DOMAIN_APP = TEMPLATE_PREFIX + "domain_app";
  public static final String DOMAIN_STATIC = TEMPLATE_PREFIX + "domain_static";

  public static final String ORGANIZATION_NAME = "organization_name";
  public static final String ORGANIZATION_WEBSITE = "organization_website";
  public static final String ORGANIZATION_LOGO = "organization_logo";

  public static final String EMPLOYEE_NAME = "employee_name";
  public static final String EMPLOYEE_FIRST_NAME = "employee_firstName";
  public static final String EMPLOYEE_PROFILE_PICTURE = "employee_profilePicture";

  public static final String DEFAULT_APPLICATION_NAME = "ExaTask";
  public static final String DEFAULT_CONTACT_EMAIL_ID = "contact@exatask.com";
  public static final String DEFAULT_DOMAIN_APP = "http://app.exatask.local";
  public static final String DEFAULT_DOMAIN_STATIC = "http://static.exatask.local";
}
