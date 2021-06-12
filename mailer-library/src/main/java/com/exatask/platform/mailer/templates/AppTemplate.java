package com.exatask.platform.mailer.templates;

public interface AppTemplate {

  String layout();

  String template();

  String subject(String... args);
}
