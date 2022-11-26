package com.gggorock.urlshortener.domain;

import com.gggorock.urlshortener.dto.UrlAccessCountOut;
import com.gggorock.urlshortener.dto.UrlIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class UrlShorteningService {

    private final UrlRepository urlRepository;
    private final UrlFactory urlFactory;

    @Autowired
    public UrlShorteningService(UrlRepository urlRepository, UrlFactory urlFactory) {
        this.urlRepository = urlRepository;
        this.urlFactory = urlFactory;
    }

    public String shorten(UrlIn urlIn) {
        String originUrl = urlIn.getUrl();
        String normalUrl = removeProtocol(originUrl);

        Url url = urlRepository
                .findByOriginUrl(normalUrl)
                .orElseGet(() -> {
                    Url newUrl = urlFactory.createUrl(normalUrl);
                    urlRepository.save(newUrl);
                    return newUrl;
                });

        return "/" + url.getShortenUrl();
    }

    public String redirect(String shortenUrl) {
        return urlRepository
                .findByShortenUrl(shortenUrl)
                .orElseThrow(RuntimeException::new)
                .increaseCount()
                .getOriginUrl();
    }

    public UrlAccessCountOut getUrlRequestCount(String shortenUrl) {
        Url url = urlRepository
                .findByShortenUrl(shortenUrl)
                .orElseThrow(RuntimeException::new);
        return UrlAccessCountOut.from(url);
    }

    public List<UrlAccessCountOut> getUrlRequestCountAll() {
        List<Url> urls = urlRepository.findAll();
        List<UrlAccessCountOut> accessCountDtos = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            accessCountDtos.add(UrlAccessCountOut.from(urls.get(i)));
        }
        return accessCountDtos;

    }

    private String removeProtocol(String originUrl) {
        if (originUrl.indexOf("http")== 0) {
            if (originUrl.indexOf('s') == 4) {
                return originUrl.substring(8);
            } else{
                return originUrl.substring(7);
            }
        }
        return originUrl;
    }
}
