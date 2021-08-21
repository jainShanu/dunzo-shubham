package com.dunzo.coffeeMachine.service;

import com.dunzo.coffeeMachine.constants.Constants.AVAILABILITY_STATUS;
import com.dunzo.coffeeMachine.inventory.ContainerManager;
import com.dunzo.coffeeMachine.models.Beverage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BeverageDispatcher {
    private Beverage beverage;
    private static final Logger logger = LoggerFactory.getLogger(BeverageDispatcher.class);

    public void dispatch(Beverage beverage) {
        Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap = ContainerManager.checkAndUpdateContainer(beverage);
        process(ingredientStatusMap);
    }

    private void process(Map<AVAILABILITY_STATUS, List<String>> ingredientStatusMap){
        if (ingredientStatusMap.containsKey(AVAILABILITY_STATUS.UNAVAILABLE)){
            System.out.println("Cannot process" + beverage.getName() + " as below ingredients unavailable:\n" +
                    ingredientStatusMap.get(AVAILABILITY_STATUS.UNAVAILABLE).toString());
        }else if (ingredientStatusMap.containsKey(AVAILABILITY_STATUS.INSUFFICIENT)){
            System.out.println("OOPS!!! Cannot process " + beverage.getName() + " as below ingredients are " +
                    "in insufficient \n" + ingredientStatusMap.get(AVAILABILITY_STATUS.INSUFFICIENT).toString());
        }
        else {
            System.out.println("Yupee! enjoy your favorite drink "+ beverage.getName());
        }
    }

}
