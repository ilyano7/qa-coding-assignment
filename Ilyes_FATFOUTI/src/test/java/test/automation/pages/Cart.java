package test.automation.pages;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Cart extends AbstractPageObject{


    private static final By CART_TITLE =By.xpath("//span[text()='Your Cart']");
    private static final By CART_INVENTORY_ITEMS =By.xpath("//div[@data-test='inventory-item']");
    private static final String CART_INVENTORY_ITEMS_NAME ="//div[text()='%s']";
    private static final By CART_CHECKOUT =By.id("checkout");

    protected Cart(SeleniumClient client, TestContext context) {
        super(client, context);
    }
    public static Cart reachAndCreate(SeleniumClient client, TestContext context) {
        Cart page = new Cart(client, context);
        page.validate();
        return page;
    }
    @Override
    protected List<By> getMandatoryElements() {
        return ImmutableList.of(CART_TITLE);
    }
    public void checkItems(String itemsList)
    {
        try{
            List<String> listOfItem = Arrays.asList(itemsList.split(","));
            Assert.assertEquals(client.getWebDriver().findElements(CART_INVENTORY_ITEMS).size(),listOfItem.size(),"Check your product list !!");
            for(String item : listOfItem)
            {
                WebElement elmItem = client.getWebDriver().findElement(By.xpath(String.format(CART_INVENTORY_ITEMS_NAME, item)));
                Assert.assertTrue(elmItem.isDisplayed(), "The product is not displayed !!");
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    public void checkout()
    {
        {
            try {
                client.getWebDriver().findElement(CART_CHECKOUT).click();
            }
            catch (Exception e)
            {
                Assert.fail(e.getMessage());
            }
        }
    }
}
