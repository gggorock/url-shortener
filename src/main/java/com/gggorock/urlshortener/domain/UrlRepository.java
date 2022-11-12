package com.gggorock.urlshortener.domain;

import java.util.List;
import java.util.Optional;

public interface UrlRepository {

    Url save(Url url);

    Optional<Url> findByShortenUrl(String shortenUrl);

    Optional<Url> findByOriginUrl(String originUrl);

    List<Url> findAll();

}

