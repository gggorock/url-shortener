package com.gggorock.urlshortener.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class Base62UrlConverterTest {

    UrlConverter urlEncoder = new Base62UrlConverter();

    @Test
    @DisplayName("숫자 인코딩결과와 encode()값이 같은지")
    void encode_encodedValue_equalExpectedValue() {
        //given
        long case1 = 1;

        //when
        String encodedValue = urlEncoder.encode(case1);

        //then
        assertThat(encodedValue).isEqualTo("B");

    }

    @Test
    @DisplayName("인코딩결과를 디코딩하면 원본값이 나온다")
    void decode_decodeEncodedValue_equalOriginValue() {
        //given
        long test = 29358905;
        String encodedValue = urlEncoder.encode(test);

        //when
        long decodedValue = urlEncoder.decode(encodedValue);

        //then
        assertThat(decodedValue).isEqualTo(test);
    }
}