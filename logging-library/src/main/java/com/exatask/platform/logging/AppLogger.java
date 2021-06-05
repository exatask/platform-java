package com.exatask.platform.logging;

import com.exatask.platform.logging.serializers.LogSerializer;
import com.exatask.platform.logging.serializers.LogSerializerFactory;
import com.exatask.platform.logging.serializers.LogSerializerType;
import com.exatask.platform.utilities.contexts.RequestContextProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.Date;

public class AppLogger {

  private final org.apache.logging.log4j.Logger log4jLogger;

  private final LogSerializer serializer;

  private final String serviceName;

  AppLogger(String clazz, String service) {

    log4jLogger = LogManager.getLogger(clazz);
    serviceName = service;

    LoggerContext loggerContext = ((org.apache.logging.log4j.core.Logger) log4jLogger).getContext();
    String style = loggerContext.getConfiguration().getStrSubstitutor().getVariableResolver().lookup("style");

    if (StringUtils.isEmpty(style)) {
      style = LogSerializerType.LINE.toString();
    }
    serializer = LogSerializerFactory.getLogSerializer(style.toUpperCase());
  }

  public void trace(String message) {
    log(Level.TRACE, message);
  }

  public void trace(Exception exception) {
    log(Level.TRACE, exception);
  }

  public void trace(AppLogMessage logMessage) {
    log(Level.TRACE, logMessage);
  }

  public void debug(String message) {
    log(Level.DEBUG, message);
  }

  public void debug(Exception exception) {
    log(Level.DEBUG, exception);
  }

  public void debug(AppLogMessage logMessage) {
    log(Level.DEBUG, logMessage);
  }

  public void info(String message) {
    log(Level.INFO, message);
  }

  public void info(Exception exception) {
    log(Level.INFO, exception);
  }

  public void info(AppLogMessage logMessage) {
    log(Level.INFO, logMessage);
  }

  public void warn(String message) {
    log(Level.WARN, message);
  }

  public void warn(Exception exception) {
    log(Level.WARN, exception);
  }

  public void warn(AppLogMessage logMessage) {
    log(Level.WARN, logMessage);
  }

  public void error(String message) {
    log(Level.ERROR, message);
  }

  public void error(Exception exception) {
    log(Level.ERROR, exception);
  }

  public void error(AppLogMessage logMessage) {
    log(Level.ERROR, logMessage);
  }

  public void fatal(String message) {
    log(Level.FATAL, message);
  }

  public void fatal(Exception exception) {
    log(Level.FATAL, exception);
  }

  public void fatal(AppLogMessage logMessage) {
    log(Level.FATAL, logMessage);
  }

  public void log(Level level, String message) {

    AppLogMessage logMessage = AppLogMessage.builder().message(message).build();
    this.log(level, logMessage);
  }

  public void log(Level level, Exception exception) {

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    this.log(level, logMessage);
  }

  public void log(Level level, AppLogMessage logMessage) {

    logMessage.setLevel(level.toString().toLowerCase())
        .setServiceName(serviceName)
        .setTraceId(RequestContextProvider.getTraceId())
        .setSpanId(RequestContextProvider.getSpanId())
        .setTimestamp(new Date());
    log4jLogger.log(level, serializer.serialize(logMessage));
  }
}
