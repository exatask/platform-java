package com.exatask.platform.mailer;

import com.exatask.platform.utilities.ApplicationContextUtility;
import lombok.experimental.UtilityClass;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@UtilityClass
public class AppMailerUtility {

  private static final String EMAIL_DIR = "emails" + File.separator;
  private static final String HTML_DIR = "html" + File.separator;
  private static final String TEXT_DIR = "text" + File.separator;
  private static final String LAYOUT_DIR = "layouts" + File.separator;
  private static final String TEMPLATE_DIR = "templates" + File.separator;

  private static final String HTML_SUFFIX = ".html";
  private static final String TEXT_SUFFIX = ".txt";

  public static SpringTemplateEngine templateEngine(ResourceBundleMessageSource messageSource, String resourceDir) {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(htmlTemplateResolver(resourceDir));
    templateEngine.addTemplateResolver(textTemplateResolver(resourceDir));
    templateEngine.setTemplateEngineMessageSource(messageSource);
    return templateEngine;
  }

  public static ITemplateResolver htmlTemplateResolver(String resourceDir) {

    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(ApplicationContextUtility.getApplicationContext());
    templateResolver.setOrder(1);
    templateResolver.setResolvablePatterns(Collections.singleton(EMAIL_DIR + HTML_DIR + "*"));
    templateResolver.setPrefix(ResourceUtils.FILE_URL_PREFIX + resourceDir);
    templateResolver.setSuffix(HTML_SUFFIX);
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    return templateResolver;
  }

  public static ITemplateResolver textTemplateResolver(String resourceDir) {

    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(ApplicationContextUtility.getApplicationContext());
    templateResolver.setOrder(2);
    templateResolver.setResolvablePatterns(Collections.singleton(EMAIL_DIR + TEXT_DIR + "*"));
    templateResolver.setPrefix(ResourceUtils.FILE_URL_PREFIX + resourceDir);
    templateResolver.setSuffix(TEXT_SUFFIX);
    templateResolver.setTemplateMode(TemplateMode.TEXT);
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    return templateResolver;
  }

  public static String prepareLayoutPath(String layout, String locale) {
    return EMAIL_DIR + HTML_DIR + LAYOUT_DIR + locale + File.separator + layout + HTML_SUFFIX;
  }

  public static String prepareTemplatePath(TemplateMode mode, String template, String locale) {

    String templateFile = template;
    templateFile += mode == TemplateMode.HTML ? HTML_SUFFIX : TEXT_SUFFIX;

    String templatePath = EMAIL_DIR;
    templatePath += mode == TemplateMode.HTML ? HTML_DIR : TEXT_DIR;
    templatePath += TEMPLATE_DIR + locale + File.separator + templateFile;
    return templatePath;
  }
}
