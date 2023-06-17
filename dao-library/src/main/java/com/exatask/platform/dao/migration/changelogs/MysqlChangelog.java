package com.exatask.platform.dao.migration.changelogs;

import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.hashes.AppHash;
import com.exatask.platform.crypto.hashes.AppHashAlgorithm;
import com.exatask.platform.crypto.hashes.AppHashFactory;
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

public abstract class MysqlChangelog extends BaseJavaMigration {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  private AppHash appHash;

  protected MysqlChangelog() throws GeneralSecurityException {

    super();
    appHash = AppHashFactory.getHash(AppHashAlgorithm.MD5, AppEncoderAlgorithm.HEX, null);
  }

  public abstract String author();

  public abstract void execute(Context context) throws Exception;

  @Override
  public void migrate(Context context) {

    ((ClassicConfiguration) context.getConfiguration()).setInstalledBy(this.author());

    try {
      this.execute(context);
    } catch (Exception exception) {
      LOGGER.error(exception);
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

    String hash = appHash.hash(clazzContent.toString());
    return new BigInteger(hash, 16).intValue();
  }
}
