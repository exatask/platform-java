package com.exatask.platform.mailer.templates;

public interface AppTemplate {

  String template();

  String subject(String... args);
}
