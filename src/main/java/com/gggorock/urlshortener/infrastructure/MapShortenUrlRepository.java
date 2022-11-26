package com.gggorock.urlshortener.infrastructure;

import com.gggorock.urlshortener.domain.ShortenUrl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class MapShortenUrlRepository implements ShortenUrlRepository {

    private final Map<String, ShortenUrl> store = new ConcurrentHashMap<>();

    @Override
    public ShortenUrl save(ShortenUrl url) {
        String key = url.getShortenUrl();
        store.put(key, url);
        return url;
    }

    @Override
    public Optional<ShortenUrl> findByShortenUrl(String shortenUrl) {
        return Optional.ofNullable(store.get(shortenUrl));
    }

    @Override
    public Optional<ShortenUrl> findByOriginUrl(String originUrl) {
        return store.values().stream()
                .filter(url -> url.getOriginUrl().equals(originUrl))
                .findAny();
    }

    @Override
    public List<ShortenUrl> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void clear() {
        store.clear();
    }

}
