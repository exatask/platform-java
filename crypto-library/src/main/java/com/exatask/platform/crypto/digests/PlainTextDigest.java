package com.exatask.platform.crypto.digests;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlainTextDigest implements AppDigest {

  @Override
  public String digest(String data) {
    return data;
  }
}
