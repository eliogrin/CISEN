package com.epam.cisen.sms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Util class for signature generation. Required for SMS gate authorization.
 */
public class SignatureUtil {
    public static String getSignature(final String login, final String password) {
        byte byteData[] = new byte[0];
        try {
            byteData = MessageDigest.getInstance("MD5").digest(password.getBytes());
            String dataForSha1 = login.concat(convertBytesToString(byteData));
            byteData = MessageDigest.getInstance("SHA1").digest(dataForSha1.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return convertBytesToString(byteData);
    }

    private static String convertBytesToString(final byte data[]) {
        StringBuilder sb = new StringBuilder();
        for (byte byteItem : data) {
            sb.append(Integer.toString((byteItem & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
