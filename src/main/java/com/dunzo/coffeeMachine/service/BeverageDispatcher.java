package com.dunzo.coffeeMachine.service;

import com.dunzo.coffeeMachine.constants.Constants.AVAILABILITY_STATUS;
import com.dunzo.coffeeMachine.inventory.ContainerManager;
import com.dunzo.coffeeMachine.models.Beverage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BeverageDispatcher implements Runnable{
    private Beverage beverage;
    private static final IndicatorService indicator = new IndicatorServiceImpl();
    private static final Logger logger = LoggerFactory.getLogger(BeverageDispatcher.class);

    public BeverageDispatcher(Beverage beverage) {this.beverage = beverage;}

    /**
     * process and then dispatch beverage
     */
    @Override
    public void run() {
        Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap = ContainerManager.checkAndUpdateContainer(beverage);
        dispatch(ingredientStatusMap);
    }

    private void dispatch(Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap){
        if (ingredientStatusMap.containsKey(AVAILABILITY_STATUS.UNAVAILABLE)) {
            System.out.println("Cannot process" + beverage.getName() + " as below ingredients unavailable:\n" +
                    ingredientStatusMap.get(AVAILABILITY_STATUS.UNAVAILABLE).toString());

            ingredientStatusMap.get(AVAILABILITY_STATUS.UNAVAILABLE).forEach(indicator::markIndicatorAsEmpty);
        }else if (ingredientStatusMap.containsKey(AVAILABILITY_STATUS.INSUFFICIENT)){
            System.out.println("OOPS!!! Cannot process " + beverage.getName() + " as below ingredients are " +
                    "in insufficient \n" + ingredientStatusMap.get(AVAILABILITY_STATUS.INSUFFICIENT).toString());

            ingredientStatusMap.get(AVAILABILITY_STATUS.INSUFFICIENT).forEach(indicator::markIndicatorAsEmpty);
        }
        else {
            System.out.println("Yupee! enjoy your favorite drink "+ beverage.getName());
        }
    }

}
