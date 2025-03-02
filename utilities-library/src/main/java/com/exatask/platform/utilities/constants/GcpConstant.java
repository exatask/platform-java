package com.exatask.platform.utilities.constants;

public interface GcpConstant {

  enum StorageAcl {

    READER,
    WRITER,
    OWNER
  }

  enum StorageClass {

    STANDARD,
    NEARLINE,
    COLDLINE,
    ARCHIVE,
    REGIONAL
  }
}
