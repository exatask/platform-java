package com.exatask.platform.storage.transports;

import com.exatask.platform.storage.exceptions.CopyFailedException;
import com.exatask.platform.storage.exceptions.DownloadFailedException;
import com.exatask.platform.storage.exceptions.UploadFailedException;
import com.exatask.platform.utilities.properties.SshProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

public class SftpTransport extends AppTransport {

  private static final String CHANNEL_TYPE = "sftp";

  private final JSch sftpClient;
  private ChannelSftp sftpChannel = null;

  public SftpTransport(SshProperties sshProperties) {

    sftpClient = new JSch();
    Properties sftProperties = new Properties();
    sftProperties.setProperty("StrictHostKeyChecking", "no");

    try {

      Session sftpSession = sftpClient.getSession(sshProperties.getUsername(), sshProperties.getHost());
      sftpSession.setPassword(sshProperties.getPassword());
      sftpSession.setConfig(sftProperties);
      sftpSession.connect();
      sftpChannel = (ChannelSftp) sftpSession.openChannel(CHANNEL_TYPE);

    } catch (JSchException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public String upload(Path inputPath, String uploadPath, Map<String, String> properties) {

    try {

      InputStream inputStream = new FileInputStream(inputPath.toFile());

      sftpChannel.connect();
      sftpChannel.put(inputStream, uploadPath);
      sftpChannel.exit();
      return AppTransportType.SFTP.getPathPrefix() + COMPONENT_SEPARATOR + uploadPath;

    } catch (JSchException | SftpException | FileNotFoundException exception) {

      LOGGER.error(exception);
      throw new UploadFailedException(uploadPath, exception);
    }
  }

  @Override
  public Path download(String downloadPath) {

    try {

      Path outputFile = Files.createTempFile(AppTransportType.SFTP.getPathPrefix(), "");

      sftpChannel.connect();
      sftpChannel.get(downloadPath, outputFile.toFile().getAbsolutePath());
      sftpChannel.exit();

      return outputFile;
    } catch (JSchException | SftpException | IOException exception) {

      LOGGER.error(exception);
      throw new DownloadFailedException(downloadPath, exception);
    }
  }

  @Override
  public String copy(String sourcePath, String destinationPath, Map<String, String> properties) {

    try {

      sftpChannel.connect();
      sftpChannel.rename(sourcePath, destinationPath);
      sftpChannel.exit();
      return AppTransportType.SFTP.getPathPrefix() + COMPONENT_SEPARATOR + destinationPath;

    } catch (JSchException | SftpException exception) {

      LOGGER.error(exception);
      throw new CopyFailedException(sourcePath, destinationPath, exception);
    }
  }
}
