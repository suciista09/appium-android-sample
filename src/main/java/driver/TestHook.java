package driver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestHook {

    private static final Logger LOGGER = LogManager.getLogger(TestHook.class);

    @Before
    public void beforeScenario(Scenario scenario){
        LOGGER.info("before Scenario");

        AppiumHelper.initialize();
    }

    @After
    public void afterScenario(Scenario scenario){
        LOGGER.info("after");
    }

}
