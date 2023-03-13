package com.iot.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

    public static String hash(byte[] input) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("MD5");
        md.update(input);
        byte[] digest = md.digest();
        return encodeHexString(digest);
    }

    public static String encodeHexString(byte[] byteArray) {
        var hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            hexStringBuffer.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit((b & 0xF), 16));
        }
        return hexStringBuffer.toString();
    }
}
