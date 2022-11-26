package com.gggorock.urlshortener.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class MapUrlRepository implements UrlRepository {

    private final Map<String, Url> store = new ConcurrentHashMap<>();

    @Override
    public Url save(Url url) {
        String key = url.getShortenUrl();
        store.put(key, url);
        return url;
    }

    @Override
    public Optional<Url> findByShortenUrl(String shortenUrl) {
        return Optional.ofNullable(store.get(shortenUrl));
    }

    @Override
    public Optional<Url> findByOriginUrl(String originUrl) {
        return store.values().stream()
                .filter(url -> url.getOriginUrl().equals(originUrl))
                .findAny();
    }

    @Override
    public List<Url> findAll() {
        return new ArrayList<>(store.values());
    }

}
