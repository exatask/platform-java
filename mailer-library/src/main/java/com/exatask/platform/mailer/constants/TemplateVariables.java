package com.exatask.platform.mailer.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TemplateVariables {

  public static final String TEMPLATE_PREFIX = "template.";

  public static final String NAME = TemplateVariables.TEMPLATE_PREFIX + "exatask.name";

  public static final String CONTACT_EMAIL_ID = TemplateVariables.TEMPLATE_PREFIX + "exatask.contact.emailId";

  public static final String DOMAIN_APP = TemplateVariables.TEMPLATE_PREFIX + "exatask.domain.app";
  public static final String DOMAIN_STATIC = TemplateVariables.TEMPLATE_PREFIX + "exatask.domain.static";

  public static final String ORGANIZATION_NAME = "organization.name";
  public static final String ORGANIZATION_WEBSITE = "organization.website";
  public static final String ORGANIZATION_LOGO = "organization.logo";

  public static final String EMPLOYEE_NAME = "employee.name";
  public static final String EMPLOYEE_FIRST_NAME = "employee.firstName";
  public static final String EMPLOYEE_PROFILE_PICTURE = "employee.profilePicture";

  public static final String DEFAULT_NAME = "ExaTask";
  public static final String DEFAULT_CONTACT_EMAIL_ID = "contact@exatask.com";
  public static final String DEFAULT_DOMAIN_APP = "http://app.exatask.local";
  public static final String DEFAULT_DOMAIN_STATIC = "http://static.exatask.local";
}
