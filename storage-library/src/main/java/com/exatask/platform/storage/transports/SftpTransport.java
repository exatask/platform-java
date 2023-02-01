package com.exatask.platform.storage.transports;

import com.exatask.platform.storage.constants.MetadataProperties;
import com.exatask.platform.storage.exceptions.CopyFailedException;
import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.storage.exceptions.UploadFailedException;
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

  private ChannelSftp sftpChannel = null;

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

    } catch (JSchException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public String upload(Path inputPath, String uploadPath, Map<MetadataProperties, String> properties) {

    try {

      validateSftpDirectory(getDirectoryPath(uploadPath));

      InputStream inputStream = new FileInputStream(inputPath.toFile());
      sftpChannel.put(inputStream, uploadPath);
      return AppTransportType.SFTP.getPathPrefix() + uploadPath;

    } catch (SftpException | FileNotFoundException exception) {

      LOGGER.error(exception);
      throw new UploadFailedException(uploadPath, exception);
    }
  }

  @Override
  public Path download(String downloadPath) {

    try {

      Path outputFile = createTempFile(AppTransportType.SFTP);
      sftpChannel.get(downloadPath.replace(AppTransportType.SFTP.getPathPrefix(), ""), outputFile.toFile().getAbsolutePath());
      return outputFile;

    } catch (SftpException | IOException exception) {

      LOGGER.error(exception);
      throw new DownloadFailedException(downloadPath, exception);
    }
  }

  @Override
  public String copy(String sourcePath, String destinationPath, Map<MetadataProperties, String> properties) {

    try {

      validateSftpDirectory(getDirectoryPath(destinationPath));

      sftpChannel.rename(sourcePath, destinationPath);
      sftpChannel.exit();
      return AppTransportType.SFTP.getPathPrefix() + destinationPath;

    } catch (SftpException exception) {

      LOGGER.error(exception);
      throw new CopyFailedException(sourcePath, destinationPath, exception);
    }
  }

  private String getDirectoryPath(String filePath) {

    String[] filePathParts = filePath.split(File.separator);
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

    String[] directoryParts = directory.split(File.separator);
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
