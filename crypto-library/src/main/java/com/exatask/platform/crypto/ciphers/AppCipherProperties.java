package com.exatask.platform.crypto.ciphers;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppCipherProperties {

    private String publicKeyFile;

    private String privateKeyFile;

    private String passphrase;

    private String key;

    private String hashKey;
}
