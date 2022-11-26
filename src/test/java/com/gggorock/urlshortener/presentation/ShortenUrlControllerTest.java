package com.gggorock.urlshortener.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gggorock.urlshortener.application.ShortenUrlService;
import com.gggorock.urlshortener.domain.NotExistingUrlException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShortenUrlController.class)
class ShortenUrlControllerTest {


    @MockBean
    ShortenUrlService shortenUrlService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @PostConstruct
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("dto url의 형식이 아니면, 404에러와 에러메시지를 반환한다.")
    void generate_notUrlFormatRequestBody() throws Exception {
        //given
        String notUrlFormatValue = "I'm not url";

        ShortenUrlCreateRequestDto notUrlFormatRequestDto = newShortenUrlCreateDto(notUrlFormatValue); // 생성자, setter없는 객체로 reflection 으로 별도함수 추출
        String requestBody = convertObjectToString(notUrlFormatRequestDto);

        //when & then
        mockMvc.perform(post("/shortenUrls")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("올바르지 않은 형식입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("dto url을 보내면, 단축 url과 201 Created를 반환한다.")
    void generate_urlShorten() throws Exception {
        String originUrl = "https://www.apple.com";
        String expectedShortenUrl = "/aewiopfj";

        ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = newShortenUrlCreateDto(originUrl);

        String requestBody = convertObjectToString(shortenUrlCreateRequestDto);

        when(shortenUrlService.shorten(shortenUrlCreateRequestDto)).thenReturn(expectedShortenUrl);

        ShortenUrlCreateResponseDto shortenUrlCreateResponseDto = new ShortenUrlCreateResponseDto(expectedShortenUrl);
        String responseBody = convertObjectToString(shortenUrlCreateResponseDto);

        mockMvc.perform(post("/shortenUrls")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody))
                .andDo(print());
    }

    private static ShortenUrlCreateRequestDto newShortenUrlCreateDto(String requestedOriginUrl) throws NoSuchFieldException, IllegalAccessException {
        ShortenUrlCreateRequestDto shortenUrlCreateRequestDto = new ShortenUrlCreateRequestDto();
        Field targetUrl = shortenUrlCreateRequestDto.getClass().getDeclaredField("url");
        targetUrl.setAccessible(true);
        targetUrl.set(shortenUrlCreateRequestDto, requestedOriginUrl);
        return shortenUrlCreateRequestDto;
    }

    @Test
    @DisplayName("없는 shortenUrl 접속수를 요청하면 404에러를 반환한다.")
    void getRequestCount_notFoundUrl_error404() throws Exception {
        String notExistingShortenUrl = "waejfik";

        when(shortenUrlService.getUrlRequestCount(notExistingShortenUrl)).thenThrow(NotExistingUrlException.class);

        mockMvc.perform(get("/shortenUrls/" + notExistingShortenUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("존재하지 않는 URL입니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("shortenUrl 접속수를 요청하면 요청수를 반환하고 200 Ok를 반환한다.")
    void getRequestCount_200Ok() throws Exception {
        String shortenUrl = "absfjg";
        String originUrl = "https://www.apple.com";

        ShortenUrlAccessCountResponseDto shortenUrlAccessCountResponseDto = new ShortenUrlAccessCountResponseDto(originUrl, 15L);
        when(shortenUrlService.getUrlRequestCount(shortenUrl)).thenReturn(shortenUrlAccessCountResponseDto);

        String responseBody = convertObjectToString(shortenUrlAccessCountResponseDto);

        mockMvc.perform(get("/shortenUrls/" + shortenUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody)) // object를 string으로 변환하여 검사하기 때문에, dto는 equals overriding 필요
                .andDo(print());
    }

    @Test
    @DisplayName("shortenUrl 접속수를 모두확인요청하면 200OK를 반환한다.")
    void getRequestCounts_200ok() {
    }


    @Test
    @DisplayName("redirect를 요청하면 원본주소로 302코드와 함께 리다이렉션된다.")
    void redirect_302withRedirectOriginUrl() throws Exception {
        String expectedShortenUrl = "https://www.naver.com";
        when(shortenUrlService.redirect(any())).thenReturn(expectedShortenUrl);

        mockMvc.perform(get("/anyUrl"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", expectedShortenUrl))
                .andDo(print());
    }

    @Test
    @DisplayName("없는 단축URL로 redirect를 요청하면 원본주소로 302코드와 함께 리다이렉션된다.")
    void redirect_getNotExistingURL_404NotFound() throws Exception {
        when(shortenUrlService.redirect(any())).thenThrow(NotExistingUrlException.class);

        mockMvc.perform(get("/notExistingShortenUrl"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("존재하지 않는 URL입니다."))
                .andDo(print());
    }

    private static String convertObjectToString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}