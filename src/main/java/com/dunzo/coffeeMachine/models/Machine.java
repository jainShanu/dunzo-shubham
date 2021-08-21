package com.dunzo.coffeeMachine.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value="machine")
@AllArgsConstructor
@NoArgsConstructor
public class Machine {
    @JsonProperty("outlets")
    private Outlet outlets;

    @JsonProperty("beverages")
    private Map<String, Map<String, Integer>> menu;

    @JsonProperty("total_items_quantity")
    private Map<String, Integer> allIngredients;

}
