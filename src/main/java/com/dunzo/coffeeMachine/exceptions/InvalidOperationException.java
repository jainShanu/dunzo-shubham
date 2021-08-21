package com.dunzo.coffeeMachine.exceptions;

public class InvalidOperationException extends CoffeeMachineException {
    private String message;
    public InvalidOperationException(String message) {
        super(message);
    }
}
