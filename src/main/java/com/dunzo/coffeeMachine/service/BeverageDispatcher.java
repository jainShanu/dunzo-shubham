package com.dunzo.coffeeMachine.service;

import com.dunzo.coffeeMachine.constants.Constants.AVAILABILITY_STATUS;
import com.dunzo.coffeeMachine.inventory.ContainerManager;
import com.dunzo.coffeeMachine.models.Beverage;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class BeverageDispatcher implements Runnable{
    private Beverage beverage;
    private static final IndicatorService indicator = new IndicatorServiceImpl();

    public BeverageDispatcher(Beverage beverage) {this.beverage = beverage;}

    /**
     * processes and then dispatch beverage
     * Each beverage dispatcher tasks added to thread
     */
    @Override
    public void run() {
        log.debug("Dispatching beverage {}", beverage.getName());
        Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap = ContainerManager.checkAndUpdateContainer(beverage);
        dispatch(ingredientStatusMap);
    }

    private void dispatch(Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap){
        if (ingredientStatusMap.containsKey(AVAILABILITY_STATUS.UNAVAILABLE)) {
            System.out.println("OOPS!! Cannot process " + beverage.getName() + " as below ingredients unavailable:\n" +
                    ingredientStatusMap.get(AVAILABILITY_STATUS.UNAVAILABLE).toString());

        }else if (ingredientStatusMap.containsKey(AVAILABILITY_STATUS.INSUFFICIENT)){
            System.out.println("OOPS!!! Cannot process " + beverage.getName() + " as below ingredients are " +
                    "insufficient \n" + ingredientStatusMap.get(AVAILABILITY_STATUS.INSUFFICIENT).toString());

        }
        else {
            System.out.println("Yupee! enjoy your favorite drink " + beverage.getName());
            return;
        }
        ingredientStatusMap.getOrDefault(AVAILABILITY_STATUS.UNAVAILABLE, new ArrayList<>()).forEach(
                indicator::markIndicatorAsEmpty);
        ingredientStatusMap.getOrDefault(AVAILABILITY_STATUS.INSUFFICIENT, new ArrayList<>()).forEach(
                indicator::markIndicatorAsCritical);
    }

}
