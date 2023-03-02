package com.exatask.platform.crypto.hashes;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlainTextHash implements AppHash {

  @Override
  public String hash(String data) {
    return data;
  }
}
