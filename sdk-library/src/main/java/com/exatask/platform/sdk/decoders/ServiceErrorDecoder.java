package com.exatask.platform.sdk.decoders;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.sdk.exceptions.SdkException;
import com.exatask.platform.sdk.responses.HttpErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ServiceErrorDecoder implements ErrorDecoder {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public Exception decode(String methodKey, Response response) {

    Request request = response.request();

    try {

      String content = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
      HttpErrorResponse errorResponse = MAPPER.readValue(content, HttpErrorResponse.class);

      return SdkException.builder()
          .methodKey(methodKey)
          .url(request.url())
          .method(request.httpMethod())
          .statusCode(response.status())
          .originalResponse(content)
          .errorResponse(errorResponse)
          .build();

    } catch (IOException exception) {

      LOGGER.error(exception);
      return FeignException.errorStatus(methodKey, response);
    }
  }
}
