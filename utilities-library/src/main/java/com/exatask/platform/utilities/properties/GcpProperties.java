package com.exatask.platform.utilities.properties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

  private static final String GCP_HOST = "gcp.host";
  private static final String GCP_ACCOUNT_KEY = "gcp.accountKey";
  private static final String GCP_PROJECT_ID = "gcp.projectId";

  private String host;

  private String projectId;

  private String accountKey;

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

  public String getHost() {

    if (StringUtils.isEmpty(this.host)) {
      this.host = ServiceUtility.getServiceProperty(GCP_HOST, null);
    }

    return this.host;
  }

  public String getProjectId() {

    if (StringUtils.isEmpty(this.projectId)) {
      this.projectId = ServiceUtility.getServiceProperty(GCP_PROJECT_ID, null);
    }

    return this.projectId;
  }

  public GoogleCredentials getCredentialsProvider() throws IOException {

    if (StringUtils.isEmpty(accountKey)) {
      accountKey = ServiceUtility.getServiceProperty(GCP_ACCOUNT_KEY);
    }

    if (StringUtils.isNotEmpty(accountKey)) {

      InputStream accountKeyStream = new ByteArrayInputStream(accountKey.getBytes(StandardCharsets.UTF_8));
      return GoogleCredentials.fromStream(accountKeyStream);

    } else {
      return GoogleCredentials.getApplicationDefault();
    }
  }
}
