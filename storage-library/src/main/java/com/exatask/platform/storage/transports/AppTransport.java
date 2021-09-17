package com.exatask.platform.storage.transports;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.storage.constants.MetadataProperties;
import com.exatask.platform.storage.constants.UploadProperties;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AppTransport {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract String upload(Path inputPath, String uploadPath, Map<UploadProperties, String> properties);

  public abstract Path download(String downloadPath);

  public abstract String copy(String sourcePath, String destinationPath, Map<UploadProperties, String> properties);

  protected Map<String, String> prepareMetadata(Path path) {

    Map<String, String> metadata = new HashMap<>();
    metadata.put(MetadataProperties.ORIGINAL_FILENAME.getKey(), path.getFileName().toString());
    return metadata;
  }
}
