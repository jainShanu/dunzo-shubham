import com.dunzo.coffeeMachine.controller.CoffeeMachineRunner;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class CoffeeMachineApplication {

    public static void main(String []args) throws IOException, InterruptedException {
        /*
            1. create file object from resources location.
            2. read and load the file content
            3. using object mapper -> create model pojo
            4. call coffee machine runner start and run
            5. stop when indicator indicates stop -> when Indicator is updated to empty and no beverage can be
            processed further
            6. refill and continue running machine
        * */
        try{
            if (args.length < 1)
                log.error("Test file name to run must be passed.");
            File file = new File(CoffeeMachineRunner.class.getClassLoader().getResource(args[0]).getFile());
            String jsonInput = FileUtils.readFileToString(file, "UTF-8");
            CoffeeMachineRunner coffeeMachineRunner = CoffeeMachineRunner.getInstance(jsonInput);
            coffeeMachineRunner.setupMachine();

            List<String> indicatorO = coffeeMachineRunner.runMachine();

            if (!indicatorO.isEmpty()) {
                log.info("Indicator Status: {}", indicatorO);
                coffeeMachineRunner.resetMachine();
                coffeeMachineRunner.refill();
            }
            coffeeMachineRunner.stopMachine();
        } catch (Exception e) {
            System.out.println(String.format("Exception occurred while running %s", e.toString()));
            System.exit(1);
        }
    }
}
