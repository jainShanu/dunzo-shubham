import com.dunzo.coffeeMachine.controller.CoffeeMachineRunner;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CoffeeMachineFunctionalTestCases {
    CoffeeMachineRunner coffeeMachine;

    @Before
    public void setUp(){
    }

    @After
    public void cleanUp(){
        coffeeMachine.resetMachine();
    }

    @Test
    public void testInput1Output() throws IOException {
        final String filePath = "input.json";
        File file = new File(CoffeeMachineRunner.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachine = CoffeeMachineRunner.getInstance(jsonInput);
        List<String> output = coffeeMachine.runMachine();
        Assert.assertEquals(coffeeMachine.getMachine().getMenu().size(),4);
    }

}
