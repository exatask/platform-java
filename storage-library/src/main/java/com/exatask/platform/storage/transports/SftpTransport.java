package com.exatask.platform.storage.transports;

import com.exatask.platform.storage.constants.MetadataProperties;
import com.exatask.platform.storage.exceptions.CopyFailedException;
import com.exatask.platform.storage.exceptions.DeleteFailedException;
import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.storage.exceptions.UploadFailedException;
import com.exatask.platform.storage.upload.CopyResponse;
import com.exatask.platform.storage.upload.MoveResponse;
import com.exatask.platform.storage.upload.UploadResponse;
import com.exatask.platform.utilities.properties.SshProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class SftpTransport extends AppTransport {

  private static final String CHANNEL_TYPE = "sftp";

  private ChannelSftp sftpChannel;

  private String sftpUrl;

  public SftpTransport(SshProperties sshProperties) {

    JSch sftpClient = new JSch();
    Properties sftProperties = new Properties();
    sftProperties.setProperty("StrictHostKeyChecking", "no");

    try {

      Session sftpSession = sftpClient.getSession(sshProperties.getUsername(), sshProperties.getHost(), sshProperties.getPort());
      sftpSession.setPassword(sshProperties.getPassword());
      sftpSession.setConfig(sftProperties);
      sftpSession.connect();
      sftpChannel = (ChannelSftp) sftpSession.openChannel(CHANNEL_TYPE);
      sftpChannel.connect();

      sftpUrl = sshProperties.getSftpUrl();

    } catch (JSchException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public UploadResponse upload(Path inputPath, String uploadPath, Map<MetadataProperties, String> properties) {

    try {

      validateSftpDirectory(getDirectoryPath(uploadPath));

      InputStream inputStream = new FileInputStream(inputPath.toFile());
      sftpChannel.put(inputStream, uploadPath);

      return UploadResponse.builder()
              .fileUrl(this.url(uploadPath, null))
              .fileUri(AppTransportType.SFTP.getPathPrefix() + uploadPath)
              .build();

    } catch (SftpException | FileNotFoundException exception) {

      LOGGER.error(exception);
      throw new UploadFailedException(uploadPath, exception);
    }
  }

  @Override
  public Path download(String downloadPath) {

    try {

      String filePath = downloadPath.replace(AppTransportType.SFTP.getPathPrefix(), "");
      Path outputFile = createTempFile(AppTransportType.SFTP);
      sftpChannel.get(filePath, outputFile.toFile().getAbsolutePath());
      return outputFile;

    } catch (SftpException | IOException exception) {

      LOGGER.error(exception);
      throw new DownloadFailedException(downloadPath, exception);
    }
  }

  @Override
  public MoveResponse move(String sourcePath, String destinationPath) {

    CopyResponse copyResponse = copy(sourcePath, destinationPath);
    delete(sourcePath);

    return MoveResponse.builder()
            .fileUrl(copyResponse.getFileUrl())
            .fileUri(copyResponse.getFileUri())
            .build();
  }

  @Override
  public CopyResponse copy(String sourcePath, String destinationPath) {

    try {

      validateSftpDirectory(getDirectoryPath(destinationPath));

      String filePath = sourcePath.replace(AppTransportType.SFTP.getPathPrefix(), "");
      sftpChannel.rename(filePath, destinationPath);

      return CopyResponse.builder()
              .fileUrl(this.url(destinationPath, null))
              .fileUri(AppTransportType.SFTP.getPathPrefix() + destinationPath)
              .build();

    } catch (SftpException exception) {

      LOGGER.error(exception);
      throw new CopyFailedException(sourcePath, destinationPath, exception);
    }
  }

  @Override
  public boolean delete(String filePath) {

    try {

      filePath = filePath.replace(AppTransportType.SFTP.getPathPrefix(), "");
      sftpChannel.rm(filePath);

    } catch (SftpException exception) {

      LOGGER.error(exception);
      throw new DeleteFailedException(filePath, exception);
    }

    return true;
  }

  @Override
  public String url(String filePath, Long ttl) {
    return this.sftpUrl + filePath;
  }

  private String getDirectoryPath(String filePath) {

    String[] filePathParts = filePath.split(FILE_SEPARATOR);
    return String.join(File.separator, Arrays.copyOfRange(filePathParts, 0, filePathParts.length - 1));
  }

  private void validateSftpDirectory(String directory) throws SftpException {

    try {
      sftpChannel.stat(directory);
    } catch (SftpException exception) {
      createSftpDirectory(directory);
    }
  }

  private void createSftpDirectory(String directory) throws SftpException {

    String[] directoryParts = directory.split(FILE_SEPARATOR);
    sftpChannel.cd(sftpChannel.getHome());

    for (String part : directoryParts) {

      try {
        sftpChannel.stat(part);
      } catch (SftpException exception) {
        sftpChannel.mkdir(part);
      }
      sftpChannel.cd(part);
    }

    sftpChannel.cd(sftpChannel.getHome());
  }
}
