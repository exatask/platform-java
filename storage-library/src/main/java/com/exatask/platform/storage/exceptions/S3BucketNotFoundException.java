package com.exatask.platform.storage.exceptions;

import lombok.Getter;

public class S3BucketNotFoundException extends RuntimeException {

  @Getter
  private final String bucket;

  public S3BucketNotFoundException(String bucket) {

    super(String.format("Bucket: %s not configured in connection", bucket));
    this.bucket = bucket;
  }
}
