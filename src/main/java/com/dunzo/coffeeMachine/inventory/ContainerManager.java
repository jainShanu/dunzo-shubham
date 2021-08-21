package com.dunzo.coffeeMachine.inventory;

import com.dunzo.coffeeMachine.constants.Constants.AVAILABILITY_STATUS;
import com.dunzo.coffeeMachine.models.Beverage;
import com.dunzo.coffeeMachine.exceptions.InvalidOperationException;
import com.dunzo.coffeeMachine.service.IndicatorService;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// make individual container as thread safe. and interaction to container through only containermanager interface
public class ContainerManager {
    /*
    container manager service does following
    1. using concurrent hashmap for storing all ingredients
    2. add to container -> updates ingredients amount
    - Making containers -> private. Restrict outside interactions
    - making addToContainer and updateAndGetBeverage synchronised ->
    -
     */
    //
    private static final Containers containers = new Containers();
//    private static IndicatorService indicator;

    public static void addToContainer(String name, Integer quantity){
        containers.addToContainer(name, quantity);
    }

    /*
    checks containers for each ingred. if lesser, marks the status else updates the quantity
     */
    public synchronized static Map<AVAILABILITY_STATUS,
            List<String>> checkAndUpdateContainer(Beverage beverage){
        Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap = new HashMap<>();
        for (Map.Entry<String, Integer> ingredient_map :beverage.getIngredients().entrySet() ) {
            Pair<String, AVAILABILITY_STATUS> res = containers.getIngredient(ingredient_map.getKey(),
                    ingredient_map.getValue());
            ingredientStatusMap.getOrDefault(res.snd, new ArrayList<>()).add(res.fst);
        };

        ContainerManager.consumeIngredients(ingredientStatusMap, beverage.getIngredients());
        return ingredientStatusMap;
    }

    public static void resetContainers(){
        containers.reset();
    }
    private static void consumeIngredients(Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap,
                                           Map<String, Integer> ingredientsMap) {
        if (!ingredientStatusMap.getOrDefault(AVAILABILITY_STATUS.UNAVAILABLE,
                new ArrayList<>()).isEmpty() || !ingredientStatusMap.getOrDefault(
                AVAILABILITY_STATUS.INSUFFICIENT, new ArrayList<>()).isEmpty()) {

            ingredientStatusMap.get(AVAILABILITY_STATUS.AVAILABLE).forEach(ingredient -> {
                containers.consumeIngredient(ingredient, -1*ingredientsMap.get(ingredient));
            });
        }
    }


    private static class Containers {
        private final ConcurrentHashMap<String, AtomicInteger> containersContent = new ConcurrentHashMap<String, AtomicInteger>();
        /*
        case 1: update quantity -> if not found in container, increment
         */
        public static void addToContainer(String name, Integer quantity){
            if (containers.containersContent.containsKey(name) &&
                    containers.containersContent.get(name).get() != 0 ||
                        quantity < 0){
                throw new InvalidOperationException("Invalid operation.");
            }
            containers.containersContent.put(name, new AtomicInteger(quantity));
        }

        public Pair<String, AVAILABILITY_STATUS> getIngredient(String ingredient, Integer quantity) {
            if (!containersContent.containsKey(ingredient) || containersContent.get(ingredient).intValue() == 0){
                return Pair.of(ingredient, AVAILABILITY_STATUS.UNAVAILABLE);
            }
          if (containersContent.get(ingredient).intValue() < quantity){
              return Pair.of(ingredient, AVAILABILITY_STATUS.INSUFFICIENT);
          }
          return Pair.of(ingredient, AVAILABILITY_STATUS.AVAILABLE);
        }

        public void consumeIngredient(String ingredient, Integer quantity) {
            containersContent.get(ingredient).addAndGet(quantity);
        }

        public void reset() {
            containersContent.clear();
        }
    }


}
