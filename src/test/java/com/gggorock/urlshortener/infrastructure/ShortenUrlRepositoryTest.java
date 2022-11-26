package com.gggorock.urlshortener.infrastructure;

import com.gggorock.urlshortener.AutoAppConfig;
import com.gggorock.urlshortener.domain.ShortenUrl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ShortenUrlRepositoryTest {


    ShortenUrlRepository shortenUrlRepository = new MapShortenUrlRepository();


    @AfterEach
    void afterEach() {
        shortenUrlRepository.clear();
    }

    @Test
    @DisplayName("Url객체가 주어지고 save했을때 Url이 같다.")
    void save() {

        //given
        String originUrl = "www.naver.com";
        String shortenUrl = "iefIFJI0";
        ShortenUrl url = new ShortenUrl(originUrl, shortenUrl);

        //when
        ShortenUrl savedUrl = shortenUrlRepository.save(url);

        //then
        assertThat(savedUrl.getOriginUrl()).isEqualTo(originUrl);
        assertThat(savedUrl.getShortenUrl()).isEqualTo(shortenUrl);
        assertThat(savedUrl.getRequestCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("저장된 Url이 주어지고 findByShortenUrl했을 때, originUrl이 같다.")
    void findByShortenUrl_givenUrl() {

        //given
        String originUrl = "www.naver.com";
        String shortenUrl = "iefIFJI0";
        ShortenUrl url = new ShortenUrl(originUrl, shortenUrl);
        ShortenUrl savedUrl = shortenUrlRepository.save(url);

        //when
        ShortenUrl foundUrl = shortenUrlRepository.findByShortenUrl(shortenUrl).get();

        //then
        assertThat(foundUrl.getOriginUrl()).isEqualTo(originUrl);

    }

    @Test
    @DisplayName("저장되지 않은Url이 있고, findByShortenUrl했을 때, null이 반환된다.")
    void findByShortenUrl_noGivenUrl() {
        //given
        String shortenUrl = "iefIFJI0";
        //when
        Optional<ShortenUrl> foundUrl = shortenUrlRepository.findByShortenUrl(shortenUrl);

        //then
        assertThat(foundUrl).isEqualTo(Optional.empty());

    }


    @Test
    @DisplayName("저장된 Url이 주어지고 findByOriginUrl했을 때, shortenUrl이 같다.")
    void findByOriginUrl_givenUrl() {
        //given
        String originUrl = "www.naver.com";
        String shortenUrl = "iefIFJI0";
        ShortenUrl url = new ShortenUrl(originUrl, shortenUrl);
        ShortenUrl savedUrl = shortenUrlRepository.save(url);

        //when
        ShortenUrl foundUrl = shortenUrlRepository.findByOriginUrl(originUrl).get();

        //then
        assertThat(foundUrl.getShortenUrl()).isEqualTo(shortenUrl);
    }

    @Test
    @DisplayName("저장되지 않은Url이 있고, findByOriginUrl했을 때, null이 반환된다.")
    void findByOriginUrl_noGivenUrl() {
        //given
        String originUrl = "www.naver.com";

        //when
        Optional<ShortenUrl> foundUrl = shortenUrlRepository.findByOriginUrl(originUrl);

        //then
        assertThat(foundUrl).isEqualTo(Optional.empty());

    }

    @Test
    @DisplayName("값을 2개 저장하고, findAll했을 때, size는 2개와 같다.")
    void findAll() {
        //given
        String originUrl1 = "www.naver.com";
        String shortenUrl1 = "iefIFJI0";
        ShortenUrl url1 = new ShortenUrl(originUrl1, shortenUrl1);
        ShortenUrl savedUrl1 = shortenUrlRepository.save(url1);


        String originUrl2 = "www.apple.com";
        String shortenUrl2 = "iefI3520";
        ShortenUrl url2 = new ShortenUrl(originUrl2, shortenUrl2);
        ShortenUrl savedUrl2 = shortenUrlRepository.save(url2);

        //when
        List<ShortenUrl> foundList = shortenUrlRepository.findAll();

        //then
        assertThat(foundList.size()).isSameAs(2);
        assertThat(savedUrl1).isEqualTo(url1);
        assertThat(savedUrl2).isEqualTo(url2);
    }
}