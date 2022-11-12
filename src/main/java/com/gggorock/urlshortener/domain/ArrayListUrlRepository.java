package com.gggorock.urlshortener.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArrayListUrlRepository implements UrlRepository {

    public final List<Url> urlList = new ArrayList<>();


    @Override
    public synchronized Url save(Url url) { // ArrayList의 스레드안정성이 문제가되는 경우로 동기화(urlList의 인덱스와 Url의 인덱스의 정합성이 모두 고려되어 메소드 전체 동기화)
            urlList.add(url);
            return url;
    }

    @Override
    public Optional<Url> findByShortenUrl(String shortenUrl) {
        return urlList.stream()
                .filter(u -> u.getShortenUrl().equals(shortenUrl))
                .findAny();
    }

    @Override
    public Optional<Url> findByOriginUrl(String originUrl) {
        return urlList.stream()
                .filter(u -> u.getOriginUrl().equals(originUrl))
                .findAny();
    }
    @Override
    public List<Url> findAll() {
        return urlList;
    }

}
