package com.dunzo.coffeeMachine.service;

import java.util.List;

public interface IndicatorService {

    public void markIndicatorAsEmpty(String ingredient);
    public void markIndicatorAsCritical(String ingredient);
    public void markIndicatorAsSufficient(String ingredient);
    public List<String> indicateCritical();
    public List<String> indicateEmpty();
    public void resetIndicators();

}
