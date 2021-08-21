package com.dunzo.coffeeMachine.models;

import java.util.concurrent.atomic.AtomicInteger;

public class Container {
    private String name;
    // add positive validation on quantity
    private AtomicInteger quantity;

    public void setQuantity(Integer quantity) throws IllegalArgumentException{
        AtomicInteger qty = new AtomicInteger(quantity);
        if (qty.get() < 0)
            throw new IllegalArgumentException(String.format("Invalid amount of ingredient %s: %d",
                    (this.name), (quantity)));

        this.quantity = qty;
    }

//    public void updateQuantity(Integer quantity) { this.quantity += quantity; }

}
