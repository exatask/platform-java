package com.exatask.platform.rabbitmq.properties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppMessageProperties {

    private final Integer delay;
}
