package com.exatask.platform.storage.transports;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.storage.constants.MetadataProperties;
import com.exatask.platform.storage.upload.CopyResponse;
import com.exatask.platform.storage.upload.MoveResponse;
import com.exatask.platform.storage.upload.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AppTransport {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();
  protected static final String FILE_SEPARATOR = "/";

  public abstract Health health();

  public abstract UploadResponse upload(Path inputPath, String uploadPath, Map<MetadataProperties, String> metadata, Map<String, String> tags);

  public abstract Path download(String filePath);

  public abstract MoveResponse move(String sourcePath, String destinationPath);

  public abstract CopyResponse copy(String sourcePath, String destinationPath);

  public abstract boolean delete(String filePath);

  public abstract String url(String filePath, Long ttl);

  protected Path createTempFile(AppTransportType transportType) throws IOException {
    return Files.createTempFile(transportType.getPathPrefix(), transportType.getFileSuffix());
  }
}
