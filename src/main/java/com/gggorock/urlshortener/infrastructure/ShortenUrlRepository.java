package com.gggorock.urlshortener.infrastructure;

import com.gggorock.urlshortener.domain.ShortenUrl;

import java.util.List;
import java.util.Optional;

public interface ShortenUrlRepository {

    ShortenUrl save(ShortenUrl url);

    Optional<ShortenUrl> findByShortenUrl(String shortenUrl);

    Optional<ShortenUrl> findByOriginUrl(String originUrl);

    List<ShortenUrl> findAll();

    void clear();

}

