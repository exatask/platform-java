package com.exatask.platform.rabbitmq;

public abstract class AppMessage {

  public abstract String exchange();

  public abstract String queue();
}
