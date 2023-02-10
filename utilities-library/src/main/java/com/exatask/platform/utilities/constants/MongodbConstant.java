package com.exatask.platform.utilities.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface MongodbConstant {

    @Getter
    @AllArgsConstructor
    enum ReadPreference {

        PRIMARY("primary"),
        PRIMARY_PREFERRED("primaryPreferred"),
        SECONDARY("secondary"),
        SECONDARY_PREFERRED("secondaryPreferred"),
        NEAREST("nearest");

        private final String value;
    }
}
