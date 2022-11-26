package com.gggorock.urlshortener.domain;

import com.gggorock.urlshortener.dto.UrlIn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UrlShorteningServiceTest {

    UrlShorteningService urlShorteningService = new UrlShorteningService(new MapUrlRepository(), new UrlFactory(new Base62UrlConverter()));
    @Test
    @DisplayName("새로운URL을 단축하면, 단축한 값과 encode된 Url값이 같다.")
    void shorten_shortenUrl_equalsEncodedUrl() {
        String originUrl = "https://www.naver.com";
        UrlIn urlIn = new UrlIn(originUrl); //캡슐화를 위해 set뿐 아니라 생성자도 안만들어 두었더니 test를 못하는 상태가 발생(url을 집어넣어 서비스객체로 전달할 방안이 없음)
        urlShorteningService.shorten(urlIn);
    }

    @Test
    @DisplayName("기존URL을 단축하면, 이전에 단축한 값과 같은 단축URL을 반환한다.")
    void shorten_shortenDuplicated_equalsShortenUrls() {

    }

    @Test
    void redirect() {
    }

    @Test
    void getUrlRequestCount() {
    }
}