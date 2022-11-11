package com.gggorock.urlshortener.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArrayListUrlRepository implements UrlRepository {

    public final List<Url> urlList = new ArrayList<>();


    @Override
    public synchronized int save(Url url) { // ArrayList의 스레드안정성이 문제가되는 경우로 동기화(urlList의 인덱스와 Url의 인덱스의 정합성이 모두 고려되어 메소드 전체 동기화)
            int index = -1;
            urlList.add(url);
            index = urlList.indexOf(url); //List가 아니라 Map이었다면 id가져올때 AtomicLong으로 별도의 sequence값을 두고 put하면 Index참조시 O(n)이 아니라 O(1)으로 가능하지 않았을까 생각됨.
            return index;
    }

    @Override
    public Optional<Url> findById(int index) {
        if (urlList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(urlList.get(index));
    }

    @Override
    public Optional<Url> findByOriginUrl(String originUrl) {
        return urlList.stream()
                .filter(u -> u.getOriginUrl().equals(originUrl))
                .findAny();
    }


    @Override
    public Optional<List<Url>> findAll() {
        return Optional.ofNullable(urlList);
    }

    @Override
    public Url update(int i, Url url) {
        return urlList.set(i, url);
    }

    @Override
    public Integer indexOf(Url url) {
        return urlList.indexOf(url);
    }
}
