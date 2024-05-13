package runner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import pages.BasePage;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src\\test\\resources\\features", glue = "steps",
 plugin = {"pretty","html:target/cucumber-reports" } , tags = "@Test123")
/*
 * *
 * Esta clase va a unir todas las funcionalidades, y los features
 */
public class Runner {
	@AfterClass
	public static void cleanDriver() {
		BasePage.closeBrowser();
	}
	
}
