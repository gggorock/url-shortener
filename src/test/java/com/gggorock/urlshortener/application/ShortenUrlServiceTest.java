package com.gggorock.urlshortener.application;

import com.gggorock.urlshortener.domain.NotExistingUrlException;
import com.gggorock.urlshortener.domain.ShortenUrl;
import com.gggorock.urlshortener.infrastructure.ShortenUrlRepository;
import com.gggorock.urlshortener.presentation.ShortenUrlAccessCountResponseDto;
import com.gggorock.urlshortener.presentation.ShortenUrlCreateRequestDto;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortenUrlServiceTest {

    @InjectMocks
    ShortenUrlService shortenUrlService;

    @Mock
    ShortenUrlRepository shortenUrlRepository;

    @Mock
    ShortenUrlFactory shortenUrlFactory;

    @Test
    @DisplayName("새로운URL을 단축하면, 단축한 값과 encode된 Url값이 같다.")
    void shorten_shortenUrl_equalsEncodedUrl() throws NoSuchFieldException, IllegalAccessException {
        //given
        String originUrl = "www.kakao.com";
        String shortenUrl = "iefIFJI0";

        //dto생성
        ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = newShortenUrlCreateRequestDto(originUrl);

        //mock객체 명세정의
        ShortenUrl returnedUrl = new ShortenUrl(originUrl, shortenUrl);
        when(shortenUrlFactory.createUrl(originUrl)).thenReturn(returnedUrl);
        when(shortenUrlRepository.findByOriginUrl(originUrl)).thenReturn(Optional.empty());
        when(shortenUrlRepository.save(shortenUrlFactory.createUrl(originUrl))).thenReturn(returnedUrl);

        //when
        String resultUrl = shortenUrlService.shorten(shortenUrlCreateRequestDto);
        String expectedUrl = "/" + shortenUrl;

        //then
        assertThat(resultUrl).isEqualTo(expectedUrl);

    }

    @Test
    @DisplayName("기존URL을 단축하면, 이전에 단축한 값과 같은 단축URL을 반환한다.")
    void shorten_shortenDuplicated_equalsShortenUrls() throws IllegalAccessException, NoSuchFieldException {
        //given
        String originUrl = "www.kakao.com";
        String shortenUrl = "iefIFJI0";

        //when
         //1. dto생성
        ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = newShortenUrlCreateRequestDto(originUrl);

        //2. mock객체 명세정의
        ShortenUrl returnedUrl = new ShortenUrl(originUrl, shortenUrl);
        when(shortenUrlRepository.findByOriginUrl(originUrl)).thenReturn(Optional.of(returnedUrl));

         //3. 서비스로직 수행
        String resultUrl = shortenUrlService.shorten(shortenUrlCreateRequestDto);
        String expectedUrl = "/" + shortenUrl;

        //then
        assertThat(resultUrl).isEqualTo(expectedUrl);

    }

    @Test
    @DisplayName("존재하지 않는 Url을 입력하면, NotExistingUrlException를 던진다.")
    void redirect_notExistingUrl_throwNpe() {
        //given
        String notExistingUrl = "awiefjfa";

        //when
        when(shortenUrlRepository.findByShortenUrl(notExistingUrl)).thenReturn(Optional.empty());

        //when & then
        assertThrows(NotExistingUrlException.class, () -> shortenUrlService.redirect(notExistingUrl));

    }

    @Test
    @DisplayName("존재하는 Url을 redirect하면, OriginUrl을 반환한다.")
    void redirect_existingUrl_equalsOriginUrl() {
        //given
        String originUrl = "www.kakao.com";
        String shortenUrl = "iefIFJI0";
        ShortenUrl url = new ShortenUrl(originUrl, shortenUrl);

        //when
        when(shortenUrlRepository.findByShortenUrl(shortenUrl)).thenReturn(Optional.of(url));
        String resultUrl = shortenUrlService.redirect(shortenUrl);

        //then
        assertThat(resultUrl).isEqualTo(originUrl);
    }

    @Test
    @DisplayName("존재하지 않는 Url 접속수를 조회하면, NotExistingUrlException을 던진다.")
    void getUrlRequestCount_notExistingUrl_throwNotExistingUrlException() {
        //given
        String notExistingUrl = "awiefjfa";

        //when
        when(shortenUrlRepository.findByShortenUrl(notExistingUrl)).thenReturn(Optional.empty());

        //then
        assertThrows(NotExistingUrlException.class, () -> shortenUrlService.getUrlRequestCount(notExistingUrl));

    }

    @Test
    @DisplayName("redirect가 일어난 Url접속수를 조회하면, 기존 접속수에 +1한 접속자수와 같다.")
    void getUrlRequestCount_redirectAndGetUrlRequestCount_equalsNplus1() {
        //given
        String originUrl = "www.kakao.com";
        String shortenUrl = "iefIFJI0";
        ShortenUrl url = new ShortenUrl(originUrl, shortenUrl);

        int givenRequestCount = 99;
        int expectedRequestCount = givenRequestCount + 1;

        for (int i = 0; i < givenRequestCount; i++) {
            url.increaseCount();
        }

        when(shortenUrlRepository.findByShortenUrl(shortenUrl)).thenReturn(Optional.of(url));
        shortenUrlService.redirect(shortenUrl);


        //when
        ShortenUrlAccessCountResponseDto urlRequestCount = shortenUrlService.getUrlRequestCount(shortenUrl);
        Long resultCount = urlRequestCount.getRequestCount();

        //then
        assertThat(resultCount).isEqualTo(expectedRequestCount);
    }

    @Test
    @DisplayName("모든 requestCount를 호출하면 등록된 Url, UrlAccessCountDto의 개수와 필드공통정보가 같다.")
    void getUrlRequestCountAll() throws NoSuchFieldException, IllegalAccessException {
        //given
        RandomString rs = new RandomString();
        int givenUrlsAmount = 5;
        List<ShortenUrl> list = new ArrayList<>();
        for (int i = 0; i < givenUrlsAmount; i++) {
            String originUrl = "www."+rs.nextString()+".com";
            String shortenUrl = rs.nextString(); //Base62UrlConverter를 쓰고싶은데 의존성을 끊으니 해결방법 필요
            list.add(new ShortenUrl(originUrl, shortenUrl));

            ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = newShortenUrlCreateRequestDto(originUrl);

            ShortenUrl returnedUrl = new ShortenUrl(originUrl, shortenUrl);
            when(shortenUrlFactory.createUrl(originUrl)).thenReturn(returnedUrl);
            when(shortenUrlRepository.findByOriginUrl(originUrl)).thenReturn(Optional.empty());
            when(shortenUrlRepository.save(shortenUrlFactory.createUrl(originUrl))).thenReturn(returnedUrl);
            shortenUrlService.shorten(shortenUrlCreateRequestDto);
        }
        when(shortenUrlRepository.findAll()).thenReturn(list);

        //when
        List<ShortenUrlAccessCountResponseDto> result = shortenUrlService.getUrlRequestCountAll();

        //then
        assertThat(result.size()).isSameAs(givenUrlsAmount);
        for (int i = 0; i < givenUrlsAmount; i++) {
            String resultOriginUrl = result.get(i).getOriginUrl();
            String expectedOriginUrl = list.get(i).getOriginUrl();
            assertThat(resultOriginUrl).isEqualTo(expectedOriginUrl);

            Long resultAccessCount = result.get(i).getRequestCount();
            Long expectedAccessCount = list.get(i).getRequestCount();
            assertThat(resultAccessCount).isEqualTo(expectedAccessCount);

        }

    }

    private static ShortenUrlCreateRequestDto newShortenUrlCreateRequestDto(String originUrl) throws NoSuchFieldException, IllegalAccessException {
        ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = new ShortenUrlCreateRequestDto();
        Field targetField = shortenUrlCreateRequestDto.getClass().getDeclaredField("url"); //생성자와 setter가 없는 dto이므로 reflection사용하여 매핑
        targetField.setAccessible(true);
        targetField.set(shortenUrlCreateRequestDto, originUrl);
        return shortenUrlCreateRequestDto;
    }
}