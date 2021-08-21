package com.dunzo.coffeeMachine.exceptions;

// check how to define multiple exception of base exception class
abstract class CoffeeMachineException extends RuntimeException {
    private String message;
    public CoffeeMachineException(String message) {
        super(message);
    }
}

