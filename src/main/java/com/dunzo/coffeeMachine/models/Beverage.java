package com.dunzo.coffeeMachine.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class Beverage {
    private String name;
    private Map<String, Integer> ingredients;

    public Beverage(String name, Map<String, Integer> ingredientsMap){
        this.name = name;
        this.ingredients = ingredientsMap;
    }

}
