package com.exatask.platform.crypto.signers;

import java.util.Map;

public interface AppSigner {

  String sign(Map<String, Object> data);

  Map<String, Object> unsign(String data);
}
