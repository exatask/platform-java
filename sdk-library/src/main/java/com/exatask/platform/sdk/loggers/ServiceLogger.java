package com.exatask.platform.sdk.loggers;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogMessage;
import com.exatask.platform.logging.AppLogger;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceLogger extends Logger {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  @Override
  protected void log(String configKey, String format, Object... args) {
    LOGGER.debug(String.format(methodTag(configKey) + format, args));
  }

  @Override
  protected void logRequest(String configKey, Level logLevel, Request request) {

    if (logLevel == Level.NONE) {
      return;
    }

    AppLogMessage logMessage = AppLogMessage.builder()
        .message("Sending HTTP request for " + methodTag(configKey))
        .build();

    logMessage.setUrl(request.url())
        .setMethod(request.httpMethod().toString());

    if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

      Map<String, Object> extraParams = new HashMap<>();
      extraParams.put("headers", request.headers());

      if (logLevel.ordinal() >= Level.FULL.ordinal() && !ObjectUtils.isEmpty(request.body())) {

        String body = request.charset() != null ? new String(request.body(), request.charset()) : "Binary Data";
        extraParams.put("body", body);
      }

      logMessage.setExtraParams(extraParams);
    }

    LOGGER.debug(logMessage);
  }

  @Override
  protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long requestTime)
      throws IOException {

    if (logLevel == Level.NONE) {
      return response;
    }

    AppLogMessage logMessage = AppLogMessage.builder()
        .message("Receiving HTTP response for " + methodTag(configKey))
        .build();

    logMessage.setUrl(response.request().url())
        .setMethod(response.request().httpMethod().toString())
        .setRequestTime(requestTime);

    if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

      Map<String, Object> extraParams = new HashMap<>();
      extraParams.put("headers", response.headers());

      if (logLevel.ordinal() >= Level.FULL.ordinal() && !ObjectUtils.isEmpty(response.body())) {

        byte[] body = Util.toByteArray(response.body().asInputStream());
        extraParams.put("body", new String(body, StandardCharsets.UTF_8));
        response = response.toBuilder().body(body).build();
      }

      logMessage.setExtraParams(extraParams);
    }

    LOGGER.debug(logMessage);
    return response;
  }

  @Override
  protected IOException logIOException(String configKey, Level logLevel, IOException exception, long requestTime) {

    AppLogMessage logMessage = AppLogMessage.builder().exception(exception).build();
    logMessage.setRequestTime(requestTime);

    if (logLevel.ordinal() < Level.FULL.ordinal()) {
      logMessage.setStackTrace(null);
    }

    LOGGER.error(logMessage);
    return exception;
  }
}
