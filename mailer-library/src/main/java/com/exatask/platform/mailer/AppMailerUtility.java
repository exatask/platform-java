package com.exatask.platform.mailer;

import lombok.experimental.UtilityClass;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@UtilityClass
public class AppMailerUtility {

  private static final String MAILER_DIR = "mailers/";

  public static SpringTemplateEngine templateEngine(ResourceBundleMessageSource messageSource) {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(htmlTemplateResolver());
    templateEngine.addTemplateResolver(textTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(messageSource);
    return templateEngine;
  }

  public static ITemplateResolver htmlTemplateResolver() {

    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setOrder(1);
    templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
    templateResolver.setPrefix(ResourceUtils.CLASSPATH_URL_PREFIX + MAILER_DIR);
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    return templateResolver;
  }

  public static ITemplateResolver textTemplateResolver() {

    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setOrder(2);
    templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
    templateResolver.setPrefix(ResourceUtils.CLASSPATH_URL_PREFIX + MAILER_DIR);
    templateResolver.setSuffix(".txt");
    templateResolver.setTemplateMode(TemplateMode.TEXT);
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    return templateResolver;
  }
}
