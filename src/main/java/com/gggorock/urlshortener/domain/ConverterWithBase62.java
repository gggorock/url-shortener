package com.gggorock.urlshortener.domain;

public class ConverterWithBase62 {
    private static final char[] BASE62_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    public static String encode(int index) {
        StringBuilder sb = new StringBuilder();
        int quotient = index;
        do {
            sb.append(BASE62_TABLE[(int) (quotient % 62)]);
            quotient /= 62;
        } while (quotient > 0);
        return sb.toString();
    }

    public static int decode(String str) {
        char[] chars = str.toCharArray();
        int result = 0;
        for (int i = 0; i < chars.length; i++) {
            result+= powInt(62,i) * indexOf(chars[i]);
        }
        return result;
    }

/**
* Math.pow 실수에 음수 양수 경우까지 계산하고 있어서 간단하게 구현
* */
    private static int powInt(int base , int exp) {
        int result = 1;

        if (exp == 0) return result;
        if (exp > 0) {
            for (int i = 0; i < exp; i++) {
                result *= base;
            }
        }
        return result;
    }

    private static int indexOf(char character) {
        for (int i = 0; i < BASE62_TABLE.length; i++) {
            if (BASE62_TABLE[i] == character) {
                return i;
            }
        }
        return -1;
    }

}
