package steps;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import page.ExamplePage;
import page.Homepage;

public class ExampleSteps implements En {
    private static final Logger LOGGER = LogManager.getLogger(ExampleSteps.class);

    public ExampleSteps() {
        Given("^I am in homepage$", () -> {
            // Write code here that turns the phrase above into concrete actions
            Homepage.firstLaunchApp();
            Homepage.isInHomepge();
            LOGGER.info("I am in homepage");
        });

    }
}
