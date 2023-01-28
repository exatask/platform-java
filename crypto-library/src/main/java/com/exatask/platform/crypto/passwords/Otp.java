package com.exatask.platform.crypto.passwords;

import com.exatask.platform.crypto.ciphers.AppCipher;
import com.exatask.platform.crypto.ciphers.AppCipherAlgorithm;
import com.exatask.platform.crypto.ciphers.AppCipherFactory;
import com.exatask.platform.crypto.encoders.AppEncoder;
import com.exatask.platform.crypto.encoders.AppEncoderAlgorithm;
import com.exatask.platform.crypto.encoders.AppEncoderFactory;
import com.exatask.platform.crypto.exceptions.PasswordGenerationException;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

public abstract class Otp implements AppPassword {

    protected static final AppLogger LOGGER = AppLogManager.getLogger();

    private static final AppEncoderAlgorithm encoderType = AppEncoderAlgorithm.HEX;
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    private final AppEncoder appEncoder = AppEncoderFactory.getEncoder(encoderType);

    protected String getOtp(int length, String message, String key, AppCipherAlgorithm cipherAlgorithm) {

        length = length < 0 ? 4 : Math.min(length, 8);

        String encryptedMessage = "";
        try {

            Map<String, String> cipherKeys = Collections.singletonMap("key", key);
            AppCipher cipher = AppCipherFactory.getCipher(cipherAlgorithm, encoderType, cipherKeys);
            encryptedMessage = cipher.encrypt(message);

        } catch (GeneralSecurityException | IOException exception) {
            LOGGER.error(exception);
            throw new PasswordGenerationException();
        }

        byte[] byteMessage = this.appEncoder.decode(encryptedMessage);
        int offset = byteMessage[byteMessage.length - 1] & 0xf;
        int binaryMessage = ((byteMessage[offset] & 0x7f) << 24)
                | ((byteMessage[offset + 1] & 0xff) << 16)
                | ((byteMessage[offset + 2] & 0xff) << 8)
                | (byteMessage[offset + 3] & 0xff);

        int otp = binaryMessage % DIGITS_POWER[length];

        StringBuilder result = new StringBuilder(Integer.toString(otp));
        while (result.length() < length) {
            result.insert(0, "0");
        }
        return result.toString();
    }
}
