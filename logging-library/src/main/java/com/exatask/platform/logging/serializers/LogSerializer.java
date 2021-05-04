package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.AppLogMessage;

public interface LogSerializer {

  LogSerializerType getType();

  String serialize(AppLogMessage message);
}
