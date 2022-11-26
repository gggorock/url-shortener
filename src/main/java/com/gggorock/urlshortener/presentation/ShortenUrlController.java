package com.gggorock.urlshortener.presentation;

import com.gggorock.urlshortener.application.ShortenUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ShortenUrlController {
    private final ShortenUrlService shortenUrlService;

    @Autowired
    public ShortenUrlController(ShortenUrlService shortenUrlService) {
        this.shortenUrlService = shortenUrlService;
    }

    @PostMapping("/shortenUrls")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ShortenUrlCreateResponseDto generate(@RequestBody @Valid ShortenUrlCreateRequestDto urlDto, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        String shortenUrl = shortenUrlService.shorten(urlDto);
        return new ShortenUrlCreateResponseDto(shortenUrl);
    }

    @GetMapping("/shortenUrls/{shortenUrl}")
    @ResponseBody
    public ShortenUrlAccessCountResponseDto getUrl(@PathVariable("shortenUrl") String shortenUrl) {
        return shortenUrlService.getUrlRequestCount(shortenUrl);
    }

    @GetMapping("/shortenUrls")
    @ResponseBody
    public List<ShortenUrlAccessCountResponseDto> getRequestCount() {
        return shortenUrlService.getUrlRequestCountAll();
    }

    @GetMapping("/{shortenUrl}")
    public ResponseEntity redirect(@PathVariable("shortenUrl") String shortenUrl) throws URISyntaxException {
        String originUrl = shortenUrlService.redirect(shortenUrl);

        URI targetUri = new URI(originUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(targetUri);

        return new ResponseEntity(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }
}
