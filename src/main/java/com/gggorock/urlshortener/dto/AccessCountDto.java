package com.gggorock.urlshortener.dto;

import com.gggorock.urlshortener.domain.Url;

public class AccessCountDto {
    private final int index;
    private final String originUrl;
    private final Long accessCount;

    public AccessCountDto(int index, String originUrl, Long accessCount) {
        this.index = index;
        this.originUrl = originUrl;
        this.accessCount = accessCount;
    }
    public String getOriginUrl() {
        return originUrl;
    }
    public Long getAccessCount() {
        return accessCount;
    }

    public static AccessCountDto from(int index, Url url) {
        String originUrl = url.getOriginUrl();
        Long requestCount = url.getRequestCount();
        return new AccessCountDto(index, originUrl, requestCount);
    }

}
