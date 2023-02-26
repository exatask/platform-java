package com.exatask.platform.utilities.constants;

public interface AwsConstant {

  enum S3Acl {

    PRIVATE,
    PUBLIC_READ,
    AUTHENTICATED_READ
  }

  enum S3Storage {

    STANDARD,
    STANDARD_IA,
    INTELLIGENT,
    GLACIER
  }
}
