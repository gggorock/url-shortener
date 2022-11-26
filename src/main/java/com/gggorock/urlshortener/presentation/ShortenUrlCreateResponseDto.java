package com.gggorock.urlshortener.presentation;

public class ShortenUrlCreateResponseDto {
    private final String shortenUrl;

    public ShortenUrlCreateResponseDto(String shortenUrl) {
        this.shortenUrl = shortenUrl;
    }

    public String getShortenUrl() {
        return shortenUrl;
    }
}
