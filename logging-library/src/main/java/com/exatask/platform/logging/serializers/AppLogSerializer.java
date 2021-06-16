package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.AppLogMessage;

public interface AppLogSerializer {

  AppLogSerializerType getType();

  String serialize(AppLogMessage message);
}
