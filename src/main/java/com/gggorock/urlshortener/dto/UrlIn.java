package com.gggorock.urlshortener.dto;


import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UrlIn {


    @NotEmpty
    @URL
    private String url;

    public UrlIn(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


}
