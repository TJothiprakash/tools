package org.jp.tools.urlshortener.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.jp.tools.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RedirectController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String longUrl = urlShortenerService.getLongUrl(shortCode);
        response.setHeader("Location", longUrl);
        response.setStatus(HttpStatus.FOUND.value()); // 302 Redirect
    }
}
