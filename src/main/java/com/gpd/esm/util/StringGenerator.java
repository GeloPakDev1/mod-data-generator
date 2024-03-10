package com.gpd.esm.util;

import java.util.Random;

public final class StringGenerator {
    private StringGenerator() {
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
    public static String generateRandomString(int length, String pattern) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = pattern.charAt(random.nextInt(pattern.length()));
            sb.append(c);
        }
        return sb.toString();
    }
}
