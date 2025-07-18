package org.jp.tools.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "org.jp.tools.urlshortener")
public class UrlShortenerExceptionHandler {

    @ExceptionHandler(InvalidURLException.class)
    public ResponseEntity<String> handleInvalidUrl(InvalidURLException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid URL: " + ex.getMessage());
    }

    @ExceptionHandler(URLNotFoundException.class)
    public ResponseEntity<String> handleUrlNotFound(URLNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Short URL not found: " + ex.getMessage());
    }

    @ExceptionHandler(DuplicateURLException.class)
    public ResponseEntity<String> handleDuplicateUrl(DuplicateURLException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Duplicate short URL: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + ex.getMessage());
    }
}
