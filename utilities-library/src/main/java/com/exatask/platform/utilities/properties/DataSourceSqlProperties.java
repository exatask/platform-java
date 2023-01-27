package com.exatask.platform.utilities.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataSourceSqlProperties extends DataSourceProperties {

    private Integer minimum;

    private Integer maximum;

    private Integer timeout;

    private Integer idleTimeout;
}
