package com.nishant.assignment.springassignment.exceptions;

public class DoesNotExistsException extends RuntimeException {

    public DoesNotExistsException(String message) {
        super(message);
    }
}
