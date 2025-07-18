package org.jp.tools.urlshortener.exception;

public class DuplicateURLException extends RuntimeException {
    public DuplicateURLException(String message) {
        super(message);
    }
}
