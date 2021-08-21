package com.dunzo.coffeeMachine.exceptions;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedTaskHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.printf("tasks.RejectedTaskHandler: The beverage request %s has been rejected by coffee machine", r.toString());
    }
}

