package com.exatask.platform.crypto.signers;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppSignerProperties {

    private String secret;
}
