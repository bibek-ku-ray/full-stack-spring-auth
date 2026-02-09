package com.bibek.backend.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public  ResourceNotFoundException(){
        super("Resouce not found!!");
    }
}
