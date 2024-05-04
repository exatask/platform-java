package com.exatask.platform.migrate.changelogs;

import com.exatask.platform.crypto.digests.AppDigest;
import com.exatask.platform.crypto.digests.AppDigestAlgorithm;
import com.exatask.platform.crypto.digests.AppDigestFactory;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

public abstract class OracleChangelog extends BaseJavaMigration {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  private AppDigest appDigest;

  protected OracleChangelog() {

    super();

    try {
      appDigest = AppDigestFactory.getDigest(AppDigestAlgorithm.MD5, AppEncoderAlgorithm.HEX, null);
    } catch (GeneralSecurityException exception) {
      LOGGER.error(exception);
    }
  }

  public abstract String author();

  public abstract void execute(Context context) throws Exception;

  @Override
  public void migrate(Context context) throws Exception {

    ((ClassicConfiguration) context.getConfiguration()).setInstalledBy(this.author());

    try {
      this.execute(context);
    } catch (Exception exception) {
      LOGGER.error(exception);
      throw exception;
    }
  }

  @Override
  public Integer getChecksum() {

    Class<?> clazz = getClass();
    String clazzName = clazz.getCanonicalName().replace(".", "/") + ".class";
    StringBuilder clazzContent = new StringBuilder();

    try (InputStream inputStream = clazz.getClassLoader().getResource(clazzName).openStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

      String i;
      while ((i = bufferedReader.readLine()) != null) {
        clazzContent.append(i);
      }

    } catch (IOException exception) {
      LOGGER.error(exception);
    }

    String digest = appDigest.digest(clazzContent.toString());
    return new BigInteger(digest, 16).intValue();
  }
}
