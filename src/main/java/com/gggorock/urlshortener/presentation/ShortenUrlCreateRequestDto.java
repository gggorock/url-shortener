package com.gggorock.urlshortener.presentation;


import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class ShortenUrlCreateRequestDto {

    @NotEmpty
    @URL(regexp = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?")
    private String url;

    public ShortenUrlCreateRequestDto() {
    }


    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortenUrlCreateRequestDto that = (ShortenUrlCreateRequestDto) o;
        return Objects.equals(url, that.url);
    }

}
