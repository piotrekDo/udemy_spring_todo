package com.example.demo.error;

public class ErrorEntity<T> {
    private int code;
    private String message;
    private T details;

    public ErrorEntity() {
    }

    public ErrorEntity(int code, String message, T details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDetails() {
        return details;
    }

    public void setDetails(T details) {
        this.details = details;
    }
}
