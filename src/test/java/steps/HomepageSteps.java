package steps;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import page.Homepage;

public class HomepageSteps implements En {

    private static final Logger LOGGER = LogManager.getLogger(HomepageSteps.class);

    public HomepageSteps() {
        // Write code here that turns the phrase above into concrete actions

        When("^I click shopping cart$", () -> {
            // Write code here that turns the phrase above into concrete actions
            LOGGER.info("cart is : " + Homepage.isCartVisible());
            Homepage.clickCart();
            LOGGER.info("I click shopping cart");
        });
    }
}
