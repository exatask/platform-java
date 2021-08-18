package com.exatask.platform.sdk.exceptions;

import com.exatask.platform.dto.responses.HttpErrorResponse;
import feign.Request;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SdkException extends RuntimeException {

  private final String methodKey;

  private final String url;

  private final Request.HttpMethod method;

  private final Integer statusCode;

  private final HttpErrorResponse errorResponse;

  private final String originalResponse;
}
