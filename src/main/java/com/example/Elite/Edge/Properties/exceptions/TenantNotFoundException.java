package com.example.Elite.Edge.Properties.exceptions;

public class TenantNotFoundException extends RuntimeException{

    public TenantNotFoundException(String message){
        super(message);
    }
}
