package com.dunzo.coffeeMachine.constants;

import java.util.List;

public class Constants {
    public enum AVAILABILITY_STATUS{
        UNAVAILABLE, INSUFFICIENT, AVAILABLE;
    }
    public enum INDICATOR_STATUS{
        EMPTY, CRITICAL;
    }
    public final static Integer MAX_REQUEST = 1000;

}
