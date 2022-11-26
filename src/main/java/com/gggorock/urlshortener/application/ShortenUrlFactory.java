package com.gggorock.urlshortener.application;

import com.gggorock.urlshortener.domain.ShortenUrl;

public interface ShortenUrlFactory {

    ShortenUrl createUrl(String originUrl);
}
