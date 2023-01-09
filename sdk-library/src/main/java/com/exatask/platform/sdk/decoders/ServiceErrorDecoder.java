package com.exatask.platform.sdk.decoders;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.exceptions.SdkException;
import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ServiceErrorDecoder implements ErrorDecoder {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  @Override
  public Exception decode(String methodKey, Response response) {

    Request request = response.request();

    try {

      String content;
      if (ObjectUtils.isEmpty(response.body())) {
        content = response.status() + " : " + response.reason();
      } else {
        content = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
      }

      return SdkException.builder()
          .methodKey(methodKey)
          .url(request.url())
          .method(request.httpMethod().toString())
          .statusCode(response.status())
          .errorResponse(content)
          .build();

    } catch (IOException exception) {

      LOGGER.error(exception);
      return FeignException.errorStatus(methodKey, response);
    }
  }
}
