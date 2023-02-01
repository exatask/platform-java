package com.exatask.platform.storage.transports;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.storage.constants.MetadataProperties;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AppTransport {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract String upload(Path inputPath, String uploadPath, Map<MetadataProperties, String> properties);

  public abstract Path download(String downloadPath);

  public abstract String copy(String sourcePath, String destinationPath, Map<MetadataProperties, String> properties);

  protected Path createTempFile(AppTransportType transportType) throws IOException {

    return Files.createTempFile(transportType.getPathPrefix(), transportType.getFileSuffix());
  }

  protected Map<String, String> prepareMetadata(Map<MetadataProperties, String> properties) {

    return properties.entrySet()
        .stream()
        .collect(Collectors.toMap(property -> property.getKey().getKey(), Map.Entry::getValue));
  }
}
