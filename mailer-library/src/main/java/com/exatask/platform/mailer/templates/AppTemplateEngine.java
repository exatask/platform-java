package com.exatask.platform.mailer.templates;

import com.exatask.platform.mailer.constants.TemplateVariables;
import com.exatask.platform.utilities.ServiceUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import software.amazon.awssdk.core.internal.util.Mimetype;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.Calendar;
import java.util.Map;

@Component
public class AppTemplateEngine {

  private static final String MIME_TYPE_HTML = "text/html; charset=UTF-8";

  private static final String TEMPLATE_PROPERTY_PREFIX = "template.";

  @Autowired
  private SpringTemplateEngine templateEngine;

  public MimeBodyPart renderText(AppTemplate template, Map<String, Object> templateVariables) throws MessagingException {

    Context templateContext = getTemplateContext(template, templateVariables);
    MimeBodyPart textBodyPart = new MimeBodyPart();

    String textBody = templateEngine.process(template.layout() + ".txt", templateContext);
    textBodyPart.setContent(textBody, Mimetype.MIMETYPE_TEXT_PLAIN);
    return textBodyPart;
  }

  public MimeBodyPart renderHtml(AppTemplate template, Map<String, Object> templateVariables) throws MessagingException {

    Context templateContext = getTemplateContext(template, templateVariables);
    MimeBodyPart htmlBodyPart = new MimeBodyPart();

    String htmlBody = templateEngine.process(template.layout() + ".html", templateContext);
    htmlBodyPart.setContent(htmlBody, MIME_TYPE_HTML);
    return htmlBodyPart;
  }

  private Context getTemplateContext(AppTemplate template, Map<String, Object> templateVariables) {

    Calendar now = Calendar.getInstance();
    templateVariables.put(TemplateVariables.YEAR, now.get(Calendar.YEAR));
    templateVariables.put(TemplateVariables.NAME, ServiceUtility.getServiceProperty(TEMPLATE_PROPERTY_PREFIX + TemplateVariables.NAME));
    templateVariables.put(TemplateVariables.CONTACT_EMAIL_ID, ServiceUtility.getServiceProperty(TEMPLATE_PROPERTY_PREFIX + TemplateVariables.CONTACT_EMAIL_ID));
    templateVariables.put(TemplateVariables.DOMAIN_STATIC, ServiceUtility.getServiceProperty(TEMPLATE_PROPERTY_PREFIX + TemplateVariables.DOMAIN_STATIC));

    if (!templateVariables.containsKey(TemplateVariables.DOMAIN_APP)) {
      templateVariables.put(TemplateVariables.DOMAIN_APP, ServiceUtility.getServiceProperty(TEMPLATE_PROPERTY_PREFIX + TemplateVariables.DOMAIN_APP));
    }

    Context templateContext = new Context();
    templateContext.setVariables(templateVariables);
    templateContext.setVariable("template", template.template());
    return templateContext;
  }
}
