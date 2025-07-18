package org.jp.tools.urlshortener.dto;

public class ShortenUrlResponse {
    private String shortUrl;

    public ShortenUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
