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
import java.nio.file.Files;
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

      Session sftpSession = sftpClient.getSession(sshProperties.getUsername(), sshProperties.getHost());
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

      InputStream inputStream = new FileInputStream(inputPath.toFile());
      String[] uploadPathParts = uploadPath.split(File.separator);
      String uploadPathDir = String.join(File.separator, Arrays.copyOfRange(uploadPathParts, 0, uploadPathParts.length - 1));

      try {
        sftpChannel.stat(uploadPathDir);
      } catch (SftpException exception) {
        createSftpDirectory(uploadPathDir);
      }

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

      AppTransportType transportType = AppTransportType.SFTP;
      Path outputFile = Files.createTempFile(transportType.getPathPrefix(), transportType.getFileSuffix());

      sftpChannel.get(downloadPath, outputFile.toFile().getAbsolutePath());
      return outputFile;

    } catch (SftpException | IOException exception) {

      LOGGER.error(exception);
      throw new DownloadFailedException(downloadPath, exception);
    }
  }

  @Override
  public String copy(String sourcePath, String destinationPath, Map<MetadataProperties, String> properties) {

    String[] destinationPathParts = destinationPath.split(File.separator);
    String destinationPathDir = String.join(File.separator, Arrays.copyOfRange(destinationPathParts, 0, destinationPathParts.length - 1));

    try {

      try {
        sftpChannel.stat(destinationPathDir);
      } catch (SftpException exception) {
        createSftpDirectory(destinationPathDir);
      }

      sftpChannel.rename(sourcePath, destinationPath);
      sftpChannel.exit();
      return AppTransportType.SFTP.getPathPrefix() + destinationPath;

    } catch (SftpException exception) {

      LOGGER.error(exception);
      throw new CopyFailedException(sourcePath, destinationPath, exception);
    }
  }

  private void createSftpDirectory(String uploadPath) throws SftpException {

    String[] uploadPathParts = uploadPath.split(File.separator);
    sftpChannel.cd(sftpChannel.getHome());

    for (String path : uploadPathParts) {

      try {
        sftpChannel.stat(path);
      } catch (SftpException exception) {
        sftpChannel.mkdir(path);
      }
      sftpChannel.cd(path);
    }

    sftpChannel.cd(sftpChannel.getHome());
  }
}
