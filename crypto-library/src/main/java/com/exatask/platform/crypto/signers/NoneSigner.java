package com.exatask.platform.crypto.signers;

import java.util.Collections;
import java.util.Map;

public class NoneSigner implements AppSigner {

  @Override
  public String sign(Map<String, Object> data) {
    return null;
  }

  @Override
  public Map<String, Object> unsign(String data) {
    return Collections.emptyMap();
  }
}
