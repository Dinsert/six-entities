package com.example.six_entities.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(Long id) {
        super("File not found, id=" + id);
    }
}
