package com.gggorock.urlshortener.domain;

import com.gggorock.urlshortener.domain.Base62UrlConverter;
import com.gggorock.urlshortener.domain.UrlConverter;
import com.gggorock.urlshortener.domain.UrlFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    UrlFactory urlFactory() {
        return new UrlFactory(urlConverter());
    }

    @Bean
    UrlConverter urlConverter() {
        return new Base62UrlConverter();
    }

}
