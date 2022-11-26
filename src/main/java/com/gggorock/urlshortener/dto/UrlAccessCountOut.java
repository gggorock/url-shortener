package com.gggorock.urlshortener.dto;

import com.gggorock.urlshortener.domain.Url;

public class UrlAccessCountOut {
    private final String originUrl;
    private final Long accessCount;

    public UrlAccessCountOut(String originUrl, Long accessCount) {
        this.originUrl = originUrl;
        this.accessCount = accessCount;
    }
    public String getOriginUrl() {
        return originUrl;
    }
    public Long getAccessCount() {
        return accessCount;
    }

    public static UrlAccessCountOut from(Url url) {
        String originUrl = url.getOriginUrl();
        Long requestCount = url.getRequestCount();
        return new UrlAccessCountOut(originUrl, requestCount);
    }

}
