package com.exatask.platform.storage.upload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MoveResponse {

    private final String fileUrl;

    private final String fileUri;
}
