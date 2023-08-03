package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.digests.AppDigestAlgorithm;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppPasswordProperties {

    private Integer step;

    private AppDigestAlgorithm digest;
}
