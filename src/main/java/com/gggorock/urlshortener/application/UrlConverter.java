package com.gggorock.urlshortener.application;

public interface UrlConverter {

    String encode(Long value);

    Long decode(String value);


}
