package org.jp.tools.urlshortener.service;

import org.jp.tools.config.AppProperties;
import org.jp.tools.redis.service.RedisService;
import org.jp.tools.urlshortener.dto.ShortenUrlRequest;
import org.jp.tools.urlshortener.dto.ShortenUrlResponse;
import org.jp.tools.urlshortener.exception.InvalidURLException;
import org.jp.tools.urlshortener.exception.URLNotFoundException;
import org.jp.tools.urlshortener.model.UrlMapping;
import org.jp.tools.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UrlShortenerService {

    @Autowired
    private AppProperties appProperties;

    private static final long CACHE_TTL_MINUTES = 15;

    @Autowired
    private UrlMappingRepository repository;

    @Autowired
    private RedisService redisService;
    public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) {
        String longUrl = request.getLongUrl();

        if (longUrl == null || !longUrl.startsWith("http")) {
            throw new InvalidURLException("URL must start with http or https");
        }

        // Optional: Check DB if already shortened
        Optional<UrlMapping> existing = repository.findByLongUrl(longUrl);
        if (existing.isPresent()) {
            String shortCode = existing.get().getShortUrl();
            return new ShortenUrlResponse(appProperties.getBaseUrl() + "/" + shortCode);
        }

        // Generate short code using UUID
        String shortCode = generateShortCodeWithUUID();

        // Save to DB
        UrlMapping mapping = new UrlMapping(longUrl, shortCode);
        repository.save(mapping);

        // Save to Redis
        redisService.set(shortCode, longUrl, CACHE_TTL_MINUTES, TimeUnit.MINUTES);

        return new ShortenUrlResponse(appProperties.getBaseUrl() + "/" + shortCode);
    }

    public String getLongUrl(String shortCode) {
        // 1. Check Redis first
        String cachedUrl = redisService.get(shortCode);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        // 2. Fallback to DB
        return repository.findByShortUrl(shortCode)
                .map(mapping -> {
                    // Optional: Increase access count
                    mapping.setAccessCount(mapping.getAccessCount() + 1);
                    repository.save(mapping);

                    // Cache it again
                    redisService.set(shortCode, mapping.getLongUrl(), CACHE_TTL_MINUTES, TimeUnit.MINUTES);

                    return mapping.getLongUrl();
                })
                .orElseThrow(() -> new URLNotFoundException("Short URL not found"));
    }

    private String generateShortCodeWithUUID() {
        String code;
        do {
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 8); // 8-char
        } while (repository.findByShortUrl(code).isPresent());
        return code;
    }
}
