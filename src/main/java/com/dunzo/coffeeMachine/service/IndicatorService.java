package com.dunzo.coffeeMachine.service;

public interface IndicatorService {

    public void markIndicatorAsEmpty(String ingredient);
    public void markIndicatorAsCritical(String ingredient);
    public void markIndicatorAsSufficient(String ingredient);
    public String indicateCritcial();
    public String indicateEmpty();
    public void resetIndicators();

}
