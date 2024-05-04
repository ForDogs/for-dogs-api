package com.fordogs.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptUtil {

    private static final String DEFAULT_ALGORITHM = "SHA-512";
    private static final Encoder encoder = Base64.getEncoder();

    public static String encode(String message) {
        return encode(message, DEFAULT_ALGORITHM);
    }

    public static String encode(String message, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] hash = messageDigest.digest(message.getBytes());

            return encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("해시 알고리즘을 초기화할 수 없습니다: " + algorithm, e);
        }
    }
}
