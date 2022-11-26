package com.gggorock.urlshortener.application;

import com.gggorock.urlshortener.domain.ShortenUrl;
import com.gggorock.urlshortener.infrastructure.ShortenUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
class RandomShortenUrlFactory implements ShortenUrlFactory {

    private final UrlConverter urlConverter;
    private final ShortenUrlRepository shortenUrlRepository;

    @Autowired
    public RandomShortenUrlFactory(UrlConverter urlConverter, ShortenUrlRepository shortenUrlRepository) {
        this.urlConverter = urlConverter;
        this.shortenUrlRepository = shortenUrlRepository;
    }

    Random random = new Random();

    @Override
    public ShortenUrl createUrl(String originUrl) {
        String shortenUrl = getUniqueShortenUrl();
        return new ShortenUrl(originUrl, shortenUrl);
    }

    private String getUniqueShortenUrl() {
        String shortenUrl;
        Optional<ShortenUrl> preparedUrl;
        do {
            shortenUrl = encodeRandomLongValue();
            preparedUrl = shortenUrlRepository.findByShortenUrl(shortenUrl);
        } while (preparedUrl.isPresent());
        return shortenUrl;
    }

    private String encodeRandomLongValue() {
        long randomLong = generatePositiveRandomLong(); // base62테이블 인덱스값이 양수이므로 변경.
        String shortenUrl = urlConverter.encode(randomLong);
        return shortenUrl;
    }

    private long generatePositiveRandomLong() {
        return Math.abs(random.nextLong());
    }
}
