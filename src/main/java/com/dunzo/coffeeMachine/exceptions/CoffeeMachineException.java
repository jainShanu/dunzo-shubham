package com.dunzo.coffeeMachine.exceptions;


public class CoffeeMachineException extends RuntimeException {
    private String message;
    public CoffeeMachineException(String message) {
        super(message);
    }
}

