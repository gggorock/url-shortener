package com.gggorock.urlshortener.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UrlEncoderWithBase62Test {

    @Test
    void encode() {
        ConverterWithBase62 urlEncoderWithBase62 = new ConverterWithBase62();

        int test = 15324235;
        String encode = urlEncoderWithBase62.encode(test);
        System.out.println("encode = " + encode);

    }

    @Test
    void decode() {
        //given
        int test = 2194853;
        String encodedValue = ConverterWithBase62.encode(test);

        //when
        int decodedValue = ConverterWithBase62.decode(encodedValue);

        //then
        assertThat(decodedValue).isEqualTo(test);
    }
}