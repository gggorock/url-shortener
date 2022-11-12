package com.gggorock.urlshortener.domain;

import java.util.Objects;

public class Url {

    private final String originUrl;
    private final String shortenUrl;

    public Url(String originUrl, String shortenUrl) {
        this.originUrl = originUrl;
        this.shortenUrl = shortenUrl;
    }

    private Long requestCount = 0L;

    public String getOriginUrl() {
        return originUrl;
    }

    public String getShortenUrl() {
        return shortenUrl;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public Url increaseCount() {
        ++this.requestCount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return originUrl.equals(url.originUrl) && shortenUrl.equals(url.shortenUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originUrl, shortenUrl);
    }
}
