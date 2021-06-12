package com.exatask.platform.mailer.templates;

public interface AppTemplate {

  String layout();

  String content();

  String subject(String... args);
}
