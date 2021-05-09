package com.exatask.platform.utilities.healthcheck;

import java.util.Set;

public interface ServiceHealthCheck {

  Set<ServiceHealthCheckData> healthCheck();

  String getName();
}
