package com.kirekov.spring.boot.test.example.exception;

public class ValidationFailedException extends RuntimeException {

  public ValidationFailedException(String message) {
    super(message);
  }

  public ValidationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
