package com.exatask.platform.mailer.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class TemplateConfig {

  private static final String MAILER_DIR = "mailers/";

  @Autowired
  private ResourceBundleMessageSource messageSource;

  @Bean
  public SpringTemplateEngine templateEngine() {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(htmlTemplateResolver());
    templateEngine.addTemplateResolver(textTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(messageSource);
    return templateEngine;
  }

  @Bean(name = "htmlTemplateResolver")
  public ITemplateResolver htmlTemplateResolver() {

    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setOrder(1);
    templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
    templateResolver.setPrefix(ResourceUtils.CLASSPATH_URL_PREFIX + MAILER_DIR);
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    return templateResolver;
  }

  @Bean(name = "textTemplateResolver")
  public ITemplateResolver textTemplateResolver() {

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
