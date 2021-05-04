package com.exatask.platform.logging.serializers;

import com.exatask.platform.logging.LogMessage;

public interface LogSerializer {

  LogSerializerType getType();

  String serialize(LogMessage message);
}
