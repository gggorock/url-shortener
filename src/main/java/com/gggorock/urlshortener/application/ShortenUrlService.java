package com.gggorock.urlshortener.application;

import com.gggorock.urlshortener.domain.NotExistingUrlException;
import com.gggorock.urlshortener.domain.ShortenUrl;
import com.gggorock.urlshortener.infrastructure.ShortenUrlRepository;
import com.gggorock.urlshortener.presentation.ShortenUrlAccessCountResponseDto;
import com.gggorock.urlshortener.presentation.ShortenUrlCreateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;
    private final ShortenUrlFactory shortenUrlFactory;

    @Autowired
    public ShortenUrlService(ShortenUrlRepository shortenUrlRepository, ShortenUrlFactory shortenUrlFactory) {
        this.shortenUrlRepository = shortenUrlRepository;
        this.shortenUrlFactory = shortenUrlFactory;
    }

    public String shorten(ShortenUrlCreateRequestDto shortenUrlCreateRequestDto) {
        String originUrl = shortenUrlCreateRequestDto.getUrl();

        ShortenUrl url = shortenUrlRepository
                .findByOriginUrl(originUrl)
                .orElseGet(() -> {
                    ShortenUrl newUrl = shortenUrlFactory.createUrl(originUrl);
                    shortenUrlRepository.save(newUrl);
                    return newUrl;
                });

        return "/" + url.getShortenUrl();
    }

    public String redirect(String shortenUrl) {
        return shortenUrlRepository
                .findByShortenUrl(shortenUrl)
                .orElseThrow(NotExistingUrlException::new)
                .increaseCount()
                .getOriginUrl();
    }

    public ShortenUrlAccessCountResponseDto getUrlRequestCount(String shortenUrl) {
        ShortenUrl url = shortenUrlRepository
                .findByShortenUrl(shortenUrl)
                .orElseThrow(NotExistingUrlException::new);
        return ShortenUrlAccessCountResponseDto.from(url);
    }

    public List<ShortenUrlAccessCountResponseDto> getUrlRequestCountAll() {
        List<ShortenUrl> urls = shortenUrlRepository.findAll();
        List<ShortenUrlAccessCountResponseDto> accessCountDtos = new ArrayList<>();

        return urls.stream().map(url -> ShortenUrlAccessCountResponseDto.from(url)).toList();

    }

}
