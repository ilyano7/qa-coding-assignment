package test.automation.clients.selenium;

import test.automation.context.TestContext;
import test.automation.utils.TestProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class SeleniumClient {

    protected static final Logger LOGGER = LogManager.getLogger(SeleniumClient.class.getSimpleName());
    private static final long DEFAULT_WAIT_IN_SECONDS = 30;
    protected WebDriver webDriver;
    protected WebDriverWait webDriverWait;
    TestContext context;

    public SeleniumClient(TestContext context) {
        this.context = context;
    }

    private void initializeWebDriver() {
        if (webDriver == null || hasDriverQuit()) {
            ChromeOptions options=new ChromeOptions();
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--enable-automation");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-extensions");
            options.addArguments("--dns-prefetch-disable");
            options.addArguments("--disable-gpu");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            if (TestProperties.isSeleniumHeadless()) {
                options.addArguments("--headless=new");
            }
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.password_manager_leak_detection", false);
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);
            webDriver = new ChromeDriver(options);
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }

    }

    private void initializeWebDriverWait() {
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_WAIT_IN_SECONDS));
    }

    private boolean hasDriverQuit() {
        return ((RemoteWebDriver) webDriver).getSessionId() == null;
    }

    public WebDriver getWebDriver() {
        if (webDriver == null || hasDriverQuit()) {
            initializeWebDriver();
            initializeWebDriverWait();
        }

        return webDriver;
    }

    public WebDriverWait getWebDriverWait() {
        if (webDriverWait == null || hasDriverQuit()) {
            initializeWebDriver();
            initializeWebDriverWait();
        }
        return webDriverWait;
    }

    public void takeScreenshot() {
        if (!(webDriver == null || hasDriverQuit())){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] screenshot =
                    ((TakesScreenshot) this.getWebDriver()).getScreenshotAs(OutputType.BYTES);
            context.getScenario().attach(screenshot, "image/png","Screenshot");
        }
    }

    public void shutdown() {
        if (webDriver != null) {
            LOGGER.info("Shutting down leftover webdriver...");
            webDriver.quit();
        }
    }
}
