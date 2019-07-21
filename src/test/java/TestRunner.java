import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = false,
        features = {"classpath:features"},
        glue = {"classpath:steps", "classpath:driver"},
        plugin = {"junit:target/report/junit/junit.xml", "pretty", "json:target/cucumber.json"},
        dryRun = false,
        tags = {"@cart"}
)
public class TestRunner {

}
