package org.jp.tools.urlshortener.controller;
import org.jp.tools.urlshortener.dto.ShortenUrlRequest;
import org.jp.tools.urlshortener.dto.ShortenUrlResponse;
import org.jp.tools.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
public class ShortenUrlController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ShortenUrlResponse shortenUrl(@RequestBody ShortenUrlRequest request) {
        return urlShortenerService.shortenUrl(request);
    }
}
