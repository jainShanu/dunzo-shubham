package com.dunzo.coffeeMachine.controller;

import com.dunzo.coffeeMachine.inventory.ContainerManager;
import com.dunzo.coffeeMachine.models.Beverage;
import com.dunzo.coffeeMachine.models.Machine;
import com.dunzo.coffeeMachine.service.BeverageDispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.*;

//check how to define constants
import static com.dunzo.coffeeMachine.constants.Constants.MAX_REQUEST;

@Getter
public class CoffeeMachineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineRunner.class);
    private static final Integer MAX_QUEUED_REQUEST = MAX_REQUEST;
    public static CoffeeMachineRunner coffeeMachineRunner;
    private HashSet<Beverage> beverages;
    private final Machine machine;
    private static Executor executor;
    private ContainerManager containerManager;

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
//        executor(new RejectedTaskHandler());

    }

    public void setupMachine(){
        // setup containers and beverages
//        machine.getAllIngredients().forEach(containerManager::addToContainer);

        machine.getMenu().forEach((name, ingredients_map) -> beverages.add(new Beverage(name, ingredients_map)));
    }

    public void runMachine(){
//        executor.execute(beverages.forEach(dispatcher::dispatch));
    }
    }
