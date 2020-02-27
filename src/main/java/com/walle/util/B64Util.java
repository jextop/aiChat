package com.walle.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class B64Util {
    public static String encode(String str) {
        if (str == null) {
            return null;
        }

        try {
            return encode(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(byte[] bytes) {
        return bytes == null ? null : Base64.encodeBase64String(bytes);
    }

    public static String decode(String str) {
        if (str == null) {
            return null;
        }

        byte[] bytes = decodeForBytes(str);
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeForBytes(String str) {
        return str == null ? null : Base64.decodeBase64(str);
    }
}
