package com.gggorock.urlshortener.presentation;

import com.gggorock.urlshortener.domain.ShortenUrl;

public class ShortenUrlAccessCountResponseDto {
    private final String originUrl;
    private final Long requestCount;

    public ShortenUrlAccessCountResponseDto(String originUrl, Long requestCount) {
        this.originUrl = originUrl;
        this.requestCount = requestCount;
    }
    public String getOriginUrl() {
        return originUrl;
    }
    public Long getRequestCount() {
        return requestCount;
    }

    public static ShortenUrlAccessCountResponseDto from(ShortenUrl url) {
        String originUrl = url.getOriginUrl();
        Long requestCount = url.getRequestCount();
        return new ShortenUrlAccessCountResponseDto(originUrl, requestCount);
    }

}
