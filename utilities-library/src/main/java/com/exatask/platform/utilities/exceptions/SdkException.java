package com.exatask.platform.utilities.exceptions;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SdkException extends RuntimeException {

  private final String methodKey;

  private final String url;

  private final String method;

  private final Integer statusCode;

  private final String errorResponse;
}
