package com.exatask.platform.sdk.constants;

import com.exatask.platform.utilities.ServiceUtility;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpClientDefaults {

  private static final String HTTP_CLIENT_PING_INTERVAL = "http.client.pingInterval";
  private static final String HTTP_CLIENT_CONNECTION_TIMEOUT = "http.client.connectionTimeout";
  private static final String HTTP_CLIENT_CALL_TIMEOUT = "http.client.callTimeout";
  private static final String HTTP_CLIENT_MAX_IDLE_CONNECTION = "http.client.maxIdleConnections";
  private static final String HTTP_CLIENT_KEEP_ALIVE_DURATION = "http.client.keepAliveDuration";
  private static final String HTTP_CLIENT_MAX_REQUESTS = "http.client.maxRequests";
  private static final String HTTP_CLIENT_MAX_REQUESTS_PER_HOST = "http.client.maxRequestsPerHost";

  private static final int PING_INTERVAL = 1;
  private static final int CONNECTION_TIMEOUT = 1;
  private static final int CALL_TIMEOUT = 5;
  private static final int MAX_IDLE_CONNECTION = 2;
  private static final int KEEP_ALIVE_DURATION = 5;
  private static final int MAX_REQUESTS = 5;
  private static final int MAX_REQUESTS_PER_HOST = 2;

  public static int pingInterval() {
    return ServiceUtility.getServiceProperty(HTTP_CLIENT_PING_INTERVAL, PING_INTERVAL);
  }

  public static int connectionTimeout() {
    return ServiceUtility.getServiceProperty(HTTP_CLIENT_CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
  }

  public static int callTimeout() {
    return ServiceUtility.getServiceProperty(HTTP_CLIENT_CALL_TIMEOUT, CALL_TIMEOUT);
  }

  public static int maxIdleConnections() {
    return ServiceUtility.getServiceProperty(HTTP_CLIENT_MAX_IDLE_CONNECTION, MAX_IDLE_CONNECTION);
  }

  public static int keepAliveDuration() {
    return ServiceUtility.getServiceProperty(HTTP_CLIENT_KEEP_ALIVE_DURATION, KEEP_ALIVE_DURATION);
  }

  public static int getMaxRequests() {
    return ServiceUtility.getServiceProperty(HTTP_CLIENT_MAX_REQUESTS, MAX_REQUESTS);
  }

  public static int getMaxRequestsPerHost() {
    return ServiceUtility.getServiceProperty(HTTP_CLIENT_MAX_REQUESTS_PER_HOST, MAX_REQUESTS_PER_HOST);
  }
}
