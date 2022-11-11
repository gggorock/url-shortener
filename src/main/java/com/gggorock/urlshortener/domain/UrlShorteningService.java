package com.gggorock.urlshortener.domain;

import com.gggorock.urlshortener.dto.AccessCountDto;
import com.gggorock.urlshortener.dto.AccessCountUrlDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UrlShorteningService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlShorteningService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        Url url = new Url("Not-Available");
    }

    public AccessCountUrlDto shorten(String originUrl) {


        String normalUrl = removeProtocol(originUrl);

        Url url = urlRepository
                .findByOriginUrl(normalUrl)
                .orElseGet(() -> {
                    Url newUrl = new Url(normalUrl);
                    urlRepository.save(newUrl);  // save반환을 index로한 이점을 살리고 싶었는데 익명클래스의 블록은 블록바깥변수를 참조하지 못하네요.(인텔리제이 자동추천은 AtomicInteger) 그래서 index를 Url도메인클래스의 변수로 두고 꺼내쓰기로 했습니다.
                    return newUrl;
                });

        String shortenUrl = "/"+ConverterWithBase62.encode(urlRepository.indexOf(url));

        return new AccessCountUrlDto(shortenUrl);
    }

    public String redirect(String shortenUrl) {
        return getUrl(shortenUrl)
                .increaseCount()
                .getOriginUrl();
    }

    public AccessCountDto getUrlRequestCount(String shortenUrl) {
        int index = ConverterWithBase62.decode(shortenUrl);
        Url url = urlRepository
                .findById(index)
                .orElseThrow(RuntimeException::new);
        return AccessCountDto.from(index, url);
    }


    private Url getUrl(String shortenUrl) {
        int id = ConverterWithBase62.decode(shortenUrl);
        return urlRepository
                .findById(id)
                .orElseThrow(RuntimeException::new);
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

    public List<AccessCountDto> getUrlRequestCountAll() {
        List<Url> urls = urlRepository.findAll().orElseThrow(RuntimeException::new);
        List<AccessCountDto> accessCountDtos = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            accessCountDtos.add(AccessCountDto.from(i, urls.get(i)));
        }
        return accessCountDtos;

    }
}
