package com.dunzo.coffeeMachine.inventory;

import com.dunzo.coffeeMachine.constants.Constants.AVAILABILITY_STATUS;
import com.dunzo.coffeeMachine.exceptions.CoffeeMachineException;
import com.dunzo.coffeeMachine.models.Beverage;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Individual container as thread safe through and interaction to container through static container manager
 * methods addToContainer, checkAndUpdateContainer.
 */
public class ContainerManager {

    private static final Containers containers = new Containers();

    public static void addToContainer(String name, Integer quantity) {
        containers.addToContainer(name, quantity);
    }

    /**
     * checks containers for each ingredient and add it to the list of ingredients stored against, availability status key
     * update containers amount if all available status.
     * returns: indicatorStatusMap
     */
    public synchronized static Map<AVAILABILITY_STATUS, List<String>> checkAndUpdateContainer(Beverage beverage) {
        Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap = new HashMap<>();
        for (Map.Entry<String, Integer> ingredient_map : beverage.getIngredients().entrySet()) {
            Pair<String, AVAILABILITY_STATUS> res = containers.getIngredient(ingredient_map.getKey(),
                    ingredient_map.getValue());
            List<String> ingredientArr = ingredientStatusMap.getOrDefault(res.snd, new ArrayList<>());
            ingredientArr.add(res.fst);
            ingredientStatusMap.put(res.snd, ingredientArr);
        }

        ContainerManager.consumeIngredients(ingredientStatusMap, beverage.getIngredients());
        return ingredientStatusMap;
    }

    /**
     * reset containers to reset all ingredients stored. defined for demoing resetting machine operation.
     */
    public static void resetContainers() {
        containers.reset();
    }

    /**
     * consume ingredients if all ingredients are in the list stored against available status key
     *
     * @param ingredientStatusMap
     * @param ingredientsMap
     */
    private static void consumeIngredients(Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap,
                                           Map<String, Integer> ingredientsMap) {
        if (ingredientStatusMap.getOrDefault(AVAILABILITY_STATUS.AVAILABLE, new ArrayList<>()).size() == ingredientsMap.size()) {

            ingredientStatusMap.get(AVAILABILITY_STATUS.AVAILABLE).forEach(ingredient -> {
                containers.consumeIngredient(ingredient, -1 * ingredientsMap.get(ingredient));
            });
        }
    }

    /**
     * Container class made private static to have one common instance stored for access across any module made private
     * to restrict access from classes except outer classes
     * <p>
     * Used concurrent hashmap to make operation on containersContent thread safe.
     * Used atomicInteger - storing quantity, to ensure thread-safe and atomic operation of updating containers amount.
     */
    private static class Containers {
        private final ConcurrentHashMap<String, AtomicInteger> containersContent = new ConcurrentHashMap<String, AtomicInteger>();

        /**
         * add to containersContent
         *
         * @param name
         * @param quantity
         */
        public void addToContainer(String name, Integer quantity) {
            if (containers.containersContent.containsKey(name) &&
                    containers.containersContent.get(name).get() != 0 ||
                    quantity < 0) {
                throw new CoffeeMachineException("Invalid operation.");
            }
            containers.containersContent.put(name, new AtomicInteger(quantity));
        }

        public Pair<String, AVAILABILITY_STATUS> getIngredient(String ingredient, Integer quantity) {
            if (!containersContent.containsKey(ingredient) || containersContent.get(ingredient).intValue() == 0) {
                return Pair.of(ingredient, AVAILABILITY_STATUS.UNAVAILABLE);
            }
            if (containersContent.get(ingredient).intValue() < quantity) {
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
