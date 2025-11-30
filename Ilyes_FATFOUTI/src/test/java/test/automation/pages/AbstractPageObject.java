package test.automation.pages;

import com.google.common.collect.ImmutableList;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPageObject {


    protected static final Logger logger = LoggerFactory.getLogger(AbstractPageObject.class);
    private static final String PAGE_REACHED = "%s reached";
    private static final String CHECKING_ELEMENT = "[ %s ] : checking presence of element...";
    private static final String ELEMENT_FOUND = "[ %s ] : element found ! Resuming...";
    protected static final By TITLE = By.xpath("//*[text()='Swag Labs']");
    SeleniumClient client;
    TestContext context;


    protected AbstractPageObject(SeleniumClient client, TestContext context) {
        this.client = client;
        this.context = context;
    }


    // Override this in case you actually need to do things to reach the page
    public void reach() {
        logger.info(String.format(PAGE_REACHED, getClass().getName()));
    }

    // This will check each mandatory element and will throw a WebDriverException if any is absent
    public void validate() {
        List<By> sharedMandatoryElements = ImmutableList.of(TITLE);

        List<By> mandatoryElements = new ArrayList<>(sharedMandatoryElements);
        mandatoryElements.addAll(getMandatoryElements());

        for (By locator : mandatoryElements) {
            logger.info(String.format(CHECKING_ELEMENT, locator.toString()));
            client.getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.info(String.format(ELEMENT_FOUND, locator.toString()));
        }
    }

    protected abstract List<By> getMandatoryElements();

}
