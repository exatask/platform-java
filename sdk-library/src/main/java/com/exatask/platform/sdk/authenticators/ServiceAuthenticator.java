package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;
import feign.RequestInterceptor;

public interface ServiceAuthenticator extends RequestInterceptor {

  ServiceAuth getAuthentication();
}
