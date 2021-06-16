package com.exatask.platform.sdk.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("appServiceClient")
public interface AppServiceClient {
}
