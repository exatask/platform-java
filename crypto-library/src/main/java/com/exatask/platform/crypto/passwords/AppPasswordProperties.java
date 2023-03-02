package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.hashes.AppHashAlgorithm;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppPasswordProperties {

    private Integer step;

    private AppHashAlgorithm hash;
}
