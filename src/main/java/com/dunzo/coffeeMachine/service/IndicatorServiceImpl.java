package com.dunzo.coffeeMachine.service;

import com.dunzo.coffeeMachine.constants.Constants.INDICATOR_STATUS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * indicator service has real-time indicators to indicate ingredients low or empty status
 * map of indicator_status and list of ingredients defined inside Indicator Class, updated through external interface,
 * markIndicatorAsEmpty, markIndicatorAsLow by dispatcher method
 *
 * Inner class defined as static and private, to allow only outer class methods to access, with same instance of
 * Indicator while accessing through any class.
 *
 */
public class IndicatorServiceImpl implements IndicatorService{
    private static final Indicator indicators = new Indicator();

    private static class Indicator{
        private static Map<INDICATOR_STATUS, List<String>> indicator;
        public Indicator(){
            indicator = new HashMap<>();
            indicator.put(INDICATOR_STATUS.EMPTY, new ArrayList<>());
            indicator.put(INDICATOR_STATUS.CRITICAL, new ArrayList<>());
        }

        public void reset(){
            indicator.clear();
        }
    }


    // Adds ingredient to the empty indicator
    @Override
    public void markIndicatorAsEmpty(String ingredient) {
        indicators.indicator.get(INDICATOR_STATUS.EMPTY).add(ingredient);
    }

    @Override
    public void markIndicatorAsCritical(String ingredient) {
        indicators.indicator.get(INDICATOR_STATUS.CRITICAL).add(ingredient);
    }

    @Override
    public void markIndicatorAsSufficient(String ingredient) {
        if (indicators.indicator.getOrDefault(INDICATOR_STATUS.EMPTY,
                new ArrayList<>()).contains(ingredient))
            indicators.indicator.get(INDICATOR_STATUS.EMPTY).remove(ingredient);

        if (indicators.indicator.getOrDefault(INDICATOR_STATUS.CRITICAL,
                new ArrayList<>()).contains(ingredient))
            indicators.indicator.get(INDICATOR_STATUS.EMPTY).remove(ingredient);
    }

    @Override
    public List<String> indicateCritical() {
        return indicators.indicator.get(INDICATOR_STATUS.CRITICAL);
    }

    @Override
    public List<String> indicateEmpty() {
        return indicators.indicator.get(INDICATOR_STATUS.EMPTY);
    }

    @Override
    public void resetIndicators(){
        indicators.reset();
    }

    @Override
    public String toString(){
        return indicators.toString();
    }
}
