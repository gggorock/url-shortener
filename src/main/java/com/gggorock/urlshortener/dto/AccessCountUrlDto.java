package com.gggorock.urlshortener.dto;

public class AccessCountUrlDto {
    private final String shortenUrl;

    public AccessCountUrlDto(String shortenUrl) {
        this.shortenUrl = shortenUrl;
    }

    public String getShortenUrl() {
        return shortenUrl;
    }
}
