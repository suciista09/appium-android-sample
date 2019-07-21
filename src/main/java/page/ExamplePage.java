package page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExamplePage {

    private static final Logger LOGGER = LogManager.getLogger(ExamplePage.class);

    public void showMessage(String message){
        LOGGER.info(message);
    }
}
