package com.gggorock.urlshortener.domain;

import java.util.List;
import java.util.Optional;

public interface UrlRepository {

    int save(Url url);

    Optional<Url> findById(int id);

    Optional<Url> findByOriginUrl(String originUrl);

    Optional<List<Url>> findAll();

    Url update(int i, Url url);

    Integer indexOf(Url url);

//    Optional<ArrayList<Url>> findAll();
}

