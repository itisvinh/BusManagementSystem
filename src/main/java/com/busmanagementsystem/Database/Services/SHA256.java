package com.busmanagementsystem.Database.Services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    private SHA256() {
    }

    public static byte[] createHashBytes(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        return hash;
    }

    public static String createHashString(byte[] hash) {
        String out = null;

        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        out = hexString.toString();

        return out;
    }

    public static String createHashString(String originalString) throws NoSuchAlgorithmException{
        return createHashString(createHashBytes(originalString));
    }
}
