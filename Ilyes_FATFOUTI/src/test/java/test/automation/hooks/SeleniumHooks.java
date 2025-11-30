package test.automation.hooks;

import test.automation.clients.selenium.SeleniumClient;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class SeleniumHooks {
	SeleniumClient seleniumClient;

	public SeleniumHooks(SeleniumClient seleniumClient) {
		this.seleniumClient = seleniumClient;
	}


	@After
	public void cleanUpSelenium(Scenario scenario) {
			if (scenario.isFailed()) {
				seleniumClient.takeScreenshot();
			}
			seleniumClient.shutdown();
		}
}
