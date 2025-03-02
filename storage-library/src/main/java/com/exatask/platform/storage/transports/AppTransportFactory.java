package com.exatask.platform.storage.transports;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.storage.exceptions.InvalidStorageException;
import com.exatask.platform.utilities.properties.AwsProperties;
import com.exatask.platform.utilities.properties.GcpProperties;
import com.exatask.platform.utilities.properties.SshProperties;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppTransportFactory {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final Map<AppTransportType, AppTransport> storageList = new EnumMap<>(AppTransportType.class);

  public static AppTransport getInstanceFromPrefix(String pathPrefix) {

    for (AppTransportType transportType : AppTransportType.values()) {
      if (transportType.getPathPrefix().equals(pathPrefix) && storageList.containsKey(transportType)) {
        return storageList.get(transportType);
      }
    }

    throw new InvalidStorageException(pathPrefix);
  }

  public static AppTransport getInstance(AppTransportType transportType, Object properties) {

    if (transportType == null) {
      throw new InvalidStorageException("null");
    }

    if (storageList.containsKey(transportType)) {
      return storageList.get(transportType);
    }

    AppTransport appTransport;
    switch (transportType) {

      case SFTP:
        appTransport = new SftpTransport((SshProperties) properties);
        break;

      case AWS:
        appTransport = new AwsTransport((AwsProperties) properties);
        break;

      case GCP:
        try {
          appTransport = new GcpTransport((GcpProperties) properties);
        } catch (IOException exception) {
          LOGGER.error(exception);
          throw new InvalidStorageException(exception.getMessage());
        }
        break;

      default:
        throw new InvalidStorageException(transportType.toString());
    }

    storageList.put(transportType, appTransport);
    return appTransport;
  }
}
