package com.gggorock.urlshortener.domain;

public class Url {


    private final String originUrl;

    private Long requestCount = 0L;

    public Url(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public Url increaseCount() {
        ++this.requestCount;
        return this;
    }
}
