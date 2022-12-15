package com.hipravin.devcompanion.repo.service;

public class ConcurrentIndexOperationException extends Exception {
    public ConcurrentIndexOperationException(String message) {
        super(message);
    }
}
