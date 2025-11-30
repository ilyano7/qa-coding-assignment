package test.automation.pages;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;
import test.automation.utils.TestProperties;
import java.time.Duration;
import java.util.List;

import static java.lang.Thread.sleep;

public class Login extends AbstractPageObject{


    private static final By LOGIN_INPUT_USER_NAME =By.id("user-name");
    private static final By LOGIN_INPUT_PASSWORD =By.id("password");
    private static final By LOGIN_BUTTON_LOGIN =By.id("login-button");

    protected Login(SeleniumClient client, TestContext context) {
        super(client, context);
    }
    @Override
    public void reach() {
        boolean found = false;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + Duration.ofSeconds(240).toMillis() && found == false) {
            try {
                logger.info("loading page " +TestProperties.getEndpointUrl());
                client.getWebDriver().get(TestProperties.getEndpointUrl());
                found = true;
            } catch (Exception e) {
                if (System.currentTimeMillis() >= startTime + Duration.ofSeconds(240).toMillis())
                    e.printStackTrace();
                else {
                    e.printStackTrace();
                    logger.warn("login failed, retry again");
                    try {
                        sleep(Duration.ofSeconds(10).toMillis());
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }
    public static Login reachAndCreate(SeleniumClient client, TestContext context) {
        Login page = new Login(client, context);
        page.reach();
        page.validate();
        return page;
    }
public void loginToTheDemo()
{
    client.getWebDriverWait()
            .until(ExpectedConditions.presenceOfElementLocated(LOGIN_INPUT_USER_NAME))
            .sendKeys(TestProperties.getUsername());
    client.getWebDriverWait()
            .until(ExpectedConditions.presenceOfElementLocated(LOGIN_INPUT_PASSWORD))
            .sendKeys(TestProperties.getPassword());
    client.getWebDriverWait()
            .until(ExpectedConditions.presenceOfElementLocated(LOGIN_BUTTON_LOGIN)).click();
}

    @Override
    protected List<By> getMandatoryElements() {
        return ImmutableList.of(LOGIN_INPUT_USER_NAME,LOGIN_INPUT_PASSWORD,LOGIN_BUTTON_LOGIN);
    }
}
