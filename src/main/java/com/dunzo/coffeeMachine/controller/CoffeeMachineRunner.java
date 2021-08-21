package com.dunzo.coffeeMachine.controller;

import com.dunzo.coffeeMachine.exceptions.RejectedTaskHandler;
import com.dunzo.coffeeMachine.inventory.ContainerManager;
import com.dunzo.coffeeMachine.models.Beverage;
import com.dunzo.coffeeMachine.models.Machine;
import com.dunzo.coffeeMachine.service.BeverageDispatcher;

import com.dunzo.coffeeMachine.service.IndicatorService;
import com.dunzo.coffeeMachine.service.IndicatorServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.*;

//check how to define constants
import static com.dunzo.coffeeMachine.constants.Constants.MAX_REQUEST;

@Getter
public class CoffeeMachineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineRunner.class);
    private static final Integer MAX_QUEUED_REQUEST = MAX_REQUEST;
    public static CoffeeMachineRunner coffeeMachineRunner;
    private final Machine machine;
    private static ThreadPoolExecutor executor;
    private static final IndicatorService indicator = new IndicatorServiceImpl();

    private final RejectedTaskHandler handler = new RejectedTaskHandler();

    public static CoffeeMachineRunner getInstance(String jsonInput) throws IOException {
        if (coffeeMachineRunner == null){
            coffeeMachineRunner = new CoffeeMachineRunner(jsonInput);
        }
            return coffeeMachineRunner;
    }

    private CoffeeMachineRunner(String jsonInput) throws IOException, IllegalArgumentException {
        System.out.println("Welcome to AWESOME COFFEE MACHINE!!");
        this.machine = new ObjectMapper().readValue(jsonInput, Machine.class);
        int outlets = this.machine.getOutlets().getCount();
        // TODO: learn more about the thread pool executor
        executor = new ThreadPoolExecutor(outlets, outlets, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(MAX_QUEUED_REQUEST));
        executor.setRejectedExecutionHandler(handler);
    }

    /*
        setting up machine -> adding all beverages to beverage
    */
    public void setupMachine(){
        machine.getAllIngredients().forEach(ContainerManager::addToContainer);
    }

    public String runMachine(){
        while (!indicator.indicateEmpty().isEmpty()) {
            machine.getMenu().forEach((name, ingredients_map) -> {
                Beverage beverage = new Beverage(name, ingredients_map);
                BeverageDispatcher dispatcher = new BeverageDispatcher(beverage);
                this.process(dispatcher);
            });
        }
        return indicator.indicateEmpty();
    }

    public void process(BeverageDispatcher dispatcher){
        executor.execute(dispatcher);
    }

    public void stopMachine(){
        executor.shutdown();
    }

    public void resetMachine(){
        System.out.println("Thanks for resetting, even I need some time to rest...pheww!!!");
        this.stopMachine();
        ContainerManager.resetContainers();
        indicator.resetIndicators();
    }


    public void refill() {
        System.out.println("Refilling the machine, kindly wait.");
        this.setupMachine();
    }
}
