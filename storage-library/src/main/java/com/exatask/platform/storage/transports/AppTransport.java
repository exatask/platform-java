package com.exatask.platform.storage.transports;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AppTransport {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();
  protected static final String COMPONENT_SEPARATOR = ":";

  public static final String DOWNLOAD_NAME_KEY = "download.name";

  public abstract String upload(Path inputPath, String uploadPath, Map<String, String> properties);

  public abstract Path download(String downloadPath);

  public abstract String copy(String sourcePath, String destinationPath, Map<String, String> properties);
}
