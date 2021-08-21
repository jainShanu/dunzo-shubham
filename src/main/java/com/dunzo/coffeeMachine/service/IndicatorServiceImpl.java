package com.dunzo.coffeeMachine.service;

import com.dunzo.coffeeMachine.constants.Constants.INDICATOR_STATUS;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicatorServiceImpl implements IndicatorService{
    private static final Indicator indicators = new Indicator();
//    private static IndicatorService instance;

    @Override
    public String toString(){
        return indicators.toString();
    }

    // private indicator class defined as singleton(only initialized once) ->
    // access to indicators is only accessed to public function
    private static class Indicator{
        private Map<INDICATOR_STATUS, List<String>> indicator;
        private Indicator(){
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
    public String indicateCritcial() {
        return indicators.indicator.get(INDICATOR_STATUS.CRITICAL).toString();
    }

    @Override
    public String indicateEmpty() {
        return indicators.indicator.get(INDICATOR_STATUS.EMPTY).toString();
    }

    @Override
    public void resetIndicators(){
        indicators.reset();
    }
}
