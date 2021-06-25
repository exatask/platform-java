package com.exatask.platform.utilities.templates;

public interface AppEmailTemplate {

  String layout();

  String content();

  String subject(String... args);
}
