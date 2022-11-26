package com.gggorock.urlshortener.domain;

import com.gggorock.urlshortener.dto.UrlAccessCountOut;
import com.gggorock.urlshortener.dto.ShortenUrlOut;
import com.gggorock.urlshortener.dto.UrlIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.BindException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class UrlController {
    private final UrlShorteningService urlShorteningService;

    @Autowired
    public UrlController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @PostMapping("/urls")
    @ResponseBody
    public ShortenUrlOut generate(@RequestBody @Valid UrlIn urlDto, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        String shortenUrl = urlShorteningService.shorten(urlDto);
        return new ShortenUrlOut(shortenUrl);
    }

    @GetMapping("/urls/{shortenUrl}")
    @ResponseBody
    public UrlAccessCountOut getUrl(@PathVariable("shortenUrl") String shortenUrl) {
        return urlShorteningService.getUrlRequestCount(shortenUrl);
    }

    @GetMapping("/urls")
    @ResponseBody
    public List<UrlAccessCountOut> getUrls() {
        return urlShorteningService.getUrlRequestCountAll();
    }

    @GetMapping("{shortenUrl}") // requestParam 사용 안하니 Controller에 지정된 요청 우선순위 해결. index.html보다 우선순위를 가져감 해결 필요
    public String redirect(@PathVariable("shortenUrl") String shortenUrl) {
        return "redirect:http://"+urlShorteningService.redirect(shortenUrl);
    }
}
