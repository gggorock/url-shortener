package com.gggorock.urlshortener.application;

import com.gggorock.urlshortener.domain.ShortenUrl;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceShortenUrlFactory {

    private final UrlConverter urlConverter;
    private final AtomicLong sequence = new AtomicLong(0L);
    private final long VOID_URL_SEQUENCE;

    public SequenceShortenUrlFactory(UrlConverter urlConverter) {
        this.urlConverter = urlConverter;
        this.VOID_URL_SEQUENCE = urlConverter.decode("urls"); //sequence의 인코딩값이 urls이면 urls Api 경로 중복으로 해당 sequence값을 건너뛰고 사용하도록 근데 Api의 path가 바뀔수도 있을텐데 어떻게 OCP를 만족할지 고민중..
    }

    public ShortenUrl createUrl(String originUrl) {
        if (sequence.get() == VOID_URL_SEQUENCE) {
            sequence.getAndIncrement();
        }

        long encodingTarget = sequence.getAndIncrement();
        String shortenUrl = urlConverter.encode(encodingTarget);
        return new ShortenUrl(originUrl, shortenUrl);
    }

}
