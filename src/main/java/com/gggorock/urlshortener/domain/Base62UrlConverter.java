package com.gggorock.urlshortener.domain;

import org.springframework.stereotype.Component;

public class Base62UrlConverter implements UrlConverter {
    private final char[] BASE62_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    @Override
    public String encode(Long value) {
        StringBuilder sb = new StringBuilder();
        Long quotient = value;
        do {
            int digitIndex = (int) (quotient % 62); // 아 이부분을 좀더 가독성있게 표현하기가 어렵네요.
            char digit = BASE62_TABLE[digitIndex];
            sb.append(digit);
            quotient /= 62;
        } while (quotient > 0);
        return sb.toString();
    }

    @Override
    public Long decode(String value) {
        char[] chars = value.toCharArray();
        long result = 0L;
        for (int i = 0; i < chars.length; i++) {
            result+= powInt(62,i) * indexOf(chars[i]);
        }
        return result;
    }

/**
* Math.pow 실수에 음수 양수 경우까지 계산하고 있어서 간단하게 구현
* */
    private Long powInt(int base , long exp) {
        long result = 1;

        if (exp == 0) return result;
        if (exp > 0) {
            for (int i = 0; i < exp; i++) {
                result *= base;
            }
        }
        return result;
    }

    private int indexOf(char character) {
        int index = 0;
        int length = BASE62_TABLE.length;
        for (index = 0; index < length; index++) {
            if (BASE62_TABLE[index] == character) {
                return index;
            }
        }
        throw new RuntimeException();
    }


}
