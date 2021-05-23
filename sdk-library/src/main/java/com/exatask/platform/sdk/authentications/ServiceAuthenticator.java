package com.exatask.platform.sdk.authentications;

import com.exatask.platform.utilities.constants.ServiceAuthentication;
import feign.RequestInterceptor;

import java.util.Map;

public interface ServiceAuthenticator {

  ServiceAuthentication getAuthentication();

  RequestInterceptor getInterceptor(Map<String, String> serviceAuthData);
}
