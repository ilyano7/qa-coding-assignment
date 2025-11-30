package test.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;


public class TestProperties {
	private static final Logger LOGGER = LogManager.getLogger(TestProperties.class.getSimpleName());
	private static final String SELENIUM_HEADLESS_MODE = "selenium.headless.mode";
	private static final String TEST_PROPERTY_SUFFIX = "test.properties";
	private static final String SECRET_PROPERTY_SUFFIX = "secret.properties";
	private static final String TEST_URL = "demo.url";
	private static final String USERNAME = "demo.username";
	private static final String PASSWORD = "demo.password";



	private static Properties testProperties;

	static {
		init();
	}

	private static void init() {

		testProperties = new Properties();

		try {
			testProperties.load(ResourceFetcher.getResourceAsStream(TEST_PROPERTY_SUFFIX));
			testProperties.load(ResourceFetcher.getResourceAsStream(SECRET_PROPERTY_SUFFIX));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		for (String propertyKey : testProperties.stringPropertyNames()) {
			String expectedEnvVariableName = convertPropertyNameToEnvName(propertyKey);
			if (System.getenv(expectedEnvVariableName) != null && !System.getenv(expectedEnvVariableName).equalsIgnoreCase("null")) {
				LOGGER.info("overwrite " + propertyKey + " by " + expectedEnvVariableName + " new val=" + System.getenv(expectedEnvVariableName));
				testProperties.setProperty(propertyKey, System.getenv(expectedEnvVariableName));
			}
		}
	}





	public static boolean isSeleniumHeadless() {
		return testProperties.getProperty(SELENIUM_HEADLESS_MODE).equalsIgnoreCase("true");
	}


	public static String getEndpointUrl() {
		return testProperties.getProperty(TEST_URL) ;
	}

	public static String getUsername() {
		return testProperties.getProperty(USERNAME);
	}



	public static String getPassword() {
		return testProperties.getProperty(PASSWORD);
	}

	// This converts the x.y.z standard syntax to an adequate X_Y_Z environment variable syntax
	private static String convertPropertyNameToEnvName(String propertyName) {
		return propertyName.replace(".", "_").toUpperCase();
	}


}
