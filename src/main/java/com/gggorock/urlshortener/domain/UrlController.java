package com.gggorock.urlshortener.domain;

import com.gggorock.urlshortener.dto.AccessCountDto;
import com.gggorock.urlshortener.dto.AccessCountUrlDto;
import com.gggorock.urlshortener.dto.UrlDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class UrlController {
    private final UrlShorteningService urlShorteningService;

    @Autowired
    public UrlController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }
    private Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/urls")
    @ResponseBody
    public AccessCountUrlDto generate(@RequestBody UrlDto urlDto) {
        return  urlShorteningService.shorten(urlDto.getUrl());
    }

    @GetMapping("/urls/{shortenUrl}")
    @ResponseBody
    public AccessCountDto getUrl(@PathVariable("shortenUrl") String shortenUrl) {
        return urlShorteningService.getUrlRequestCount(shortenUrl);
    }

    @GetMapping("/urls")
    @ResponseBody
    public List<AccessCountDto> getUrls() {
        return urlShorteningService.getUrlRequestCountAll();
    }

    //url 경로에 있는게 아니라서 컨트롤러를 분리해야하지 않을까 고민
    @GetMapping("{shortenUrl}") // requestParam 사용 안하니 Controller에 지정된 요청 우선순위 해결. index.html보다 우선순위를 가져감 해결 필요
    public String redirect(@PathVariable("shortenUrl") String shortenUrl) {
        return "redirect:http://"+urlShorteningService.redirect(shortenUrl);
    }


}
