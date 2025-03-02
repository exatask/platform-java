package com.exatask.platform.utilities.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.constants.GcpConstant;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GcpProperties {

  private static final String GCP_KEY_FILE_PATH = "gcp.keyFilePath";

  private String keyFilePath;

  @Getter
  private List<GcpProperties.StorageProperties> storage;

  @Data
  @Accessors(chain = true)
  public static class StorageProperties {

    private String bucketKey;

    private String bucketName;

    private GcpConstant.StorageAcl acl = GcpConstant.StorageAcl.READER;

    private GcpConstant.StorageClass storageClass = GcpConstant.StorageClass.STANDARD;
  }

  public GoogleCredentials getCredentialsProvider() throws IOException {

    if (StringUtils.isEmpty(keyFilePath)) {
      keyFilePath = ServiceUtility.getServiceProperty(GCP_KEY_FILE_PATH);
    }

    if (StringUtils.isNotEmpty(keyFilePath)) {
      return GoogleCredentials.fromStream(new FileInputStream(keyFilePath));
    } else {
      return GoogleCredentials.getApplicationDefault();
    }
  }
}
