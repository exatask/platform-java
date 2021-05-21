package com.exatask.platform.mailer.templates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;

@Component
public class AppTemplateEngine {

  @Autowired
  @Qualifier("htmlTemplateEngine")
  private SpringTemplateEngine htmlTemplateEngine;

  @Autowired
  @Qualifier("txtTemplateEngine")
  private SpringTemplateEngine txtTemplateEngine;

  public String renderHtml(String template, Map<String, Object> templateVariables) {

    Context templateContext = new Context();
    templateContext.setVariables(templateVariables);
    return htmlTemplateEngine.process(template, templateContext);
  }

  public String renderText(String template, Map<String, Object> templateVariables) {

    Context templateContext = new Context();
    templateContext.setVariables(templateVariables);
    return txtTemplateEngine.process(template, templateContext);
  }
}
