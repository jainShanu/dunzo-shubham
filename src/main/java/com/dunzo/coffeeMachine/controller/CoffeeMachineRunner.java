package com.dunzo.coffeeMachine.controller;

import com.dunzo.coffeeMachine.exceptions.RejectedTaskHandler;
import com.dunzo.coffeeMachine.inventory.ContainerManager;
import com.dunzo.coffeeMachine.models.Beverage;
import com.dunzo.coffeeMachine.models.Machine;
import com.dunzo.coffeeMachine.service.BeverageDispatcher;

import com.dunzo.coffeeMachine.service.IndicatorService;
import com.dunzo.coffeeMachine.service.IndicatorServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

import static com.dunzo.coffeeMachine.constants.Constants.MAX_REQUEST;

/**
 * CoffeeMachineRunner defined as central controller to setup and run machine.
 * Defined as singleton.
 */
@Getter
@Slf4j
public class CoffeeMachineRunner {

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

    /**
     * setting up machine -> updating containers with the provided content through
     * static addContainer method of Container Manager
    */
    public void setupMachine(){
        machine.getAllIngredients().forEach(ContainerManager::addToContainer);
    }

    /**
     * run machine adds runnable beverage dispatcher task for each item in menu
     * returns: indicators empty status, i.e., list of ingredients that got finished after running machine
     */
    public List<String> runMachine(){
        machine.getMenu().forEach((name, ingredients_map) -> {
            Beverage beverage = new Beverage(name, ingredients_map);
            BeverageDispatcher dispatcher = new BeverageDispatcher(beverage);
            this.process(dispatcher);
        });
        return indicator.indicateEmpty();
    }

    public void process(BeverageDispatcher dispatcher){
        executor.execute(dispatcher);
    }

    /**
     * stop machine method to quit all executor tasks queued.
     */
    public void stopMachine(){
        executor.shutdown();
    }

    /**
     * stops all the dispatcher tasks
     * resets indicators and containers items quantity
     */
    public void resetMachine(){
        log.info("Thanks for resetting, even I need some time to rest...pheww!!!");
        this.stopMachine();
        ContainerManager.resetContainers();
        indicator.resetIndicators();
    }

    /**
     * Refill method defined for demo purpose -> updates the containers with the initial ingredients quantity
     */
    public void refill() {
        System.out.println("Refilling the machine, kindly wait.");
        this.setupMachine();
    }
}
