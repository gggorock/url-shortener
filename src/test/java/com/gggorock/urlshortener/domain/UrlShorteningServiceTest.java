package com.gggorock.urlshortener.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlShorteningServiceTest {

    UrlRepository urlRepository = new ArrayListUrlRepository();
    UrlShorteningService urlShorteningService = new UrlShorteningService(urlRepository);
    @Test
    void shortenNew() {
        String originUrl = "https://www.naver.com";



    }

    @Test
    void shortenDuplicated() {

    }

    @Test
    void redirect() {
    }

    @Test
    void getUrlRequestCount() {
    }
}