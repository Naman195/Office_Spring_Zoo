package com.example.naman.exceptions;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
