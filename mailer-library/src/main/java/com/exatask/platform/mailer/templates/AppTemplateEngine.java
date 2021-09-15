package com.exatask.platform.mailer.templates;

import com.exatask.platform.mailer.constants.TemplateVariables;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import com.exatask.platform.utilities.templates.AppEmailTemplate;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templatemode.TemplateMode;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppTemplateEngine {

  private static final MimeType MIME_TYPE_TEXT = new MimeType(MimeTypeUtils.TEXT_PLAIN, StandardCharsets.UTF_8);
  private static final MimeType MIME_TYPE_HTML = new MimeType(MimeTypeUtils.TEXT_HTML, StandardCharsets.UTF_8);

  private static final String TEXT_DIR = "text/";
  private static final String HTML_DIR = "html/";
  private static final String LAYOUT_DIR = "layouts/";
  private static final String CONTENT_DIR = "contents/";

  private final Map<String, Object> defaultVariables = new HashMap<>();

  private final ITemplateEngine templateEngine;

  public AppTemplateEngine(ITemplateEngine engine) {

    this.templateEngine = engine;

    String applicationNameKey = TemplateVariables.APPLICATION_NAME.replace(TemplateVariables.TEMPLATE_SEPARATOR, TemplateVariables.PROPERTY_SEPARATOR);
    String contactEmailIdKey = TemplateVariables.CONTACT_EMAIL_ID.replace(TemplateVariables.TEMPLATE_SEPARATOR, TemplateVariables.PROPERTY_SEPARATOR);
    String domainAppKey = TemplateVariables.DOMAIN_APP.replace(TemplateVariables.TEMPLATE_SEPARATOR, TemplateVariables.PROPERTY_SEPARATOR);
    String domainStaticKey = TemplateVariables.DOMAIN_STATIC.replace(TemplateVariables.TEMPLATE_SEPARATOR, TemplateVariables.PROPERTY_SEPARATOR);

    Calendar now = Calendar.getInstance();
    defaultVariables.put(TemplateVariables.TIMESTAMP_YEAR, now.get(Calendar.YEAR));

    defaultVariables.put(TemplateVariables.APPLICATION_NAME, ServiceUtility.getServiceProperty(applicationNameKey, TemplateVariables.DEFAULT_APPLICATION_NAME));
    defaultVariables.put(TemplateVariables.CONTACT_EMAIL_ID, ServiceUtility.getServiceProperty(contactEmailIdKey, TemplateVariables.DEFAULT_CONTACT_EMAIL_ID));
    defaultVariables.put(TemplateVariables.DOMAIN_APP, ServiceUtility.getServiceProperty(domainAppKey, TemplateVariables.DEFAULT_DOMAIN_APP));
    defaultVariables.put(TemplateVariables.DOMAIN_STATIC, ServiceUtility.getServiceProperty(domainStaticKey, TemplateVariables.DEFAULT_DOMAIN_STATIC));
  }

  public MimeBodyPart renderText(AppEmailTemplate template, Map<String, Object> templateVariables) throws MessagingException {

    IContext templateContext = getTemplateContext(template, TEXT_DIR, templateVariables);
    TemplateSpec templateSpec = new TemplateSpec(templateContext.getVariable("content").toString(), TemplateMode.TEXT);
    MimeBodyPart textBodyPart = new MimeBodyPart();

    String textBody = templateEngine.process(templateSpec, templateContext);
    textBodyPart.setContent(textBody, MIME_TYPE_TEXT.toString());
    return textBodyPart;
  }

  public MimeBodyPart renderHtml(AppEmailTemplate template, Map<String, Object> templateVariables) throws MessagingException {

    IContext templateContext = getTemplateContext(template, HTML_DIR, templateVariables);
    TemplateSpec templateSpec = new TemplateSpec(HTML_DIR + LAYOUT_DIR + template.layout(), TemplateMode.HTML);
    MimeBodyPart htmlBodyPart = new MimeBodyPart();

    String htmlBody = templateEngine.process(templateSpec, templateContext);
    htmlBodyPart.setContent(htmlBody, MIME_TYPE_HTML.toString());
    return htmlBodyPart;
  }

  private IContext getTemplateContext(AppEmailTemplate template, String templateBaseDir, Map<String, Object> templateVariables) {

    Map<String, Object> variables = new HashMap<>(templateVariables);
    variables.putAll(defaultVariables);

    if (!templateVariables.containsKey(TemplateVariables.ORGANIZATION_NAME)) {
      variables.put(TemplateVariables.ORGANIZATION_NAME, RequestContextProvider.getOrganizationName());
    }

    if (!templateVariables.containsKey(TemplateVariables.EMPLOYEE_NAME)) {
      variables.put(TemplateVariables.EMPLOYEE_NAME, RequestContextProvider.getEmployeeName());
    }

    Context templateContext = new Context();
    templateContext.setVariables(variables);
    templateContext.setVariable("content", templateBaseDir + CONTENT_DIR + template.content());
    return templateContext;
  }
}
