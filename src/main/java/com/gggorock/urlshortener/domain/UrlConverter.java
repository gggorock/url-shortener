package com.gggorock.urlshortener.domain;

public interface UrlConverter {

    String encode(Long value);

    Long decode(String value);


}
