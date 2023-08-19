package com.exatask.platform.crypto.ciphers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppCipherProperties {

    private String publicKeyFile;

    private String privateKeyFile;

    private String publicKey;

    private String privateKey;

    private String passphrase;

    private String key;

    private String digestKey;
}
