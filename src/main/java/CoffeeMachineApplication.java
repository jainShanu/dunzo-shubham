import com.dunzo.coffeeMachine.controller.CoffeeMachineRunner;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class CoffeeMachineApplication {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachineApplication.class);

    public static void main(String []args) throws IOException {
        /* read file name from the input.
            1. create file object from resources location.
            2. read and load the file content
            3. using object mapper -> create model pojo
            4. call coffee machine runner start and run
            5. stop when indicator indicates stop -> when Indicator is updated to empty and no beverage can be
            processed further
            6. refill and continue running machine
        * */

        if (args.length < 1)
            logger.error("Test file name to run must be passed.");
        File file = new File(CoffeeMachineRunner.class.getClassLoader().getResource(args[0]).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        CoffeeMachineRunner coffeeMachineRunner = CoffeeMachineRunner.getInstance(jsonInput);
//        System.out.println(coffeeMachineRunner.getMachine().toString());
        coffeeMachineRunner.setupMachine();
        String indicatorO = coffeeMachineRunner.runMachine();
        System.out.println(indicatorO);

        coffeeMachineRunner.resetMachine();
        coffeeMachineRunner.refill();
    }
}
