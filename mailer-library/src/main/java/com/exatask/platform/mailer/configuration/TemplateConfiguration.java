package com.exatask.platform.mailer.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class TemplateConfiguration {

  @Autowired
  private ResourceBundleMessageSource messageSource;

  @Bean(name = "htmlTemplateEngine")
  public SpringTemplateEngine htmlTemplateEngine() {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(htmlTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(messageSource);
    return templateEngine;
  }

  @Bean(name = "textTemplateEngine")
  public SpringTemplateEngine txtTemplateEngine() {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(txtTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(messageSource);
    return templateEngine;
  }

  @Bean(name = "htmlTemplateResolver")
  public ITemplateResolver htmlTemplateResolver() {

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("mailers/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  @Bean(name = "textTemplateResolver")
  public ITemplateResolver txtTemplateResolver() {

    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix("mailers/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.TEXT);
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }
}
