package com.exatask.platform.crypto.encoders;

import com.exatask.platform.crypto.constants.CryptoService;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class HexEncoder implements AppEncoder {

  protected static final AppLogger LOGGER = AppLogManager.getLogger(CryptoService.LOGGER_NAME);

  @Override
  public String encode(byte[] data) {
    return Hex.encodeHexString(data);
  }

  @Override
  public byte[] decode(String data) {
    try {
      return Hex.decodeHex(data);
    } catch (DecoderException exception) {
      LOGGER.error(exception);
    }
    return data.getBytes();
  }
}
