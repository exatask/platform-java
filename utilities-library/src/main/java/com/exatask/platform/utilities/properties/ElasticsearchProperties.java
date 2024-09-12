package com.exatask.platform.utilities.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;

@Data
@EqualsAndHashCode(callSuper = true)
public class ElasticsearchProperties extends ElasticsearchRestClientProperties {
}
