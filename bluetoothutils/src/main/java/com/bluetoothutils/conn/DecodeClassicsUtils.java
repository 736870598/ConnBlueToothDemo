package com.bluetoothutils.conn;

import java.util.Locale;

/**
 * Created by sunxiaoyu on 2017/2/10.
 */

public class DecodeClassicsUtils {

    /**
     * Hex -> Byte
     */
    public static byte[] hex2Byte(String hex) throws Exception{
        hex = hex.trim().replace(" ", "").toUpperCase(Locale.US);
        byte[] baKeyword = new byte[hex.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            baKeyword[i] = (byte) (0xFF & Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16));
        }
        return baKeyword;
    }

    /**
     * Byte -> Hex
     */
    public static String byte2Hex(byte[] bytes, int count) throws Exception{
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() % 2 == 1) {
                sb.append("0");
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

}
