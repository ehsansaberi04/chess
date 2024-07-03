package com.example.chess.Exceptions;

public class NoHomeException extends Exception{
    private String message = "home not found";
    public NoHomeException (String message) {
        this.message = message ;
    }
    public NoHomeException () {

    }
    @Override
    public String getMessage() {
        return message;
    }
}
