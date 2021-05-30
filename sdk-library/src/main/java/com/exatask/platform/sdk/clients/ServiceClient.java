package com.exatask.platform.sdk.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("serviceClient")
public interface ServiceClient {
}
