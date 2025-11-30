package test.automation.pages;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Products extends AbstractPageObject{


    private static final By PRODUCTS_TITLE =By.xpath("//span[text()='Products']");
    private static final By LIST_OF_PRODUCTS =By.id("inventory_container");
    private static final By SORT_SELECT=By.xpath("//select[@data-test='product-sort-container']");
    private static final By PRICES_LIST=By.xpath("//div[@data-test='inventory-item-price']");
    private static final String PRODUCT_ADD_BUTTON="//button[contains(@name,'%s')]";
    private static final By SHOPPING_CART_LINK=By.xpath("//a[@data-test='shopping-cart-link']");
    private static final By PRODUCT_REMOVE_BUTTON =By.xpath("//button[text()='Remove']");
    protected Products(SeleniumClient client, TestContext context) {
        super(client, context);
    }
    public static Products reachAndCreate(SeleniumClient client, TestContext context) {
        Products page = new Products(client, context);
        page.validate();
        return page;
    }
    @Override
    protected List<By> getMandatoryElements() {
        return ImmutableList.of(PRODUCTS_TITLE,LIST_OF_PRODUCTS);
    }
    public void sortItems(String text)
    {
        WebElement selectWb=client.getWebDriver().findElement(SORT_SELECT);
        Select select=new Select(selectWb);
        select.selectByVisibleText(text);
    }
    public void checkSortedItems()
    {
        List<WebElement> priceElements = client.getWebDriver().findElements(PRICES_LIST);

        // Extract prices
        List<Double> prices = priceElements.stream()
                .map(WebElement::getText)
                .map(text -> text.replace("$", "").replace(",", ".").trim())
                .map(Double::parseDouble)
                .collect(Collectors.toList());

        // Create a sorted copy
        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        // Assert the result
        Assert.assertEquals(prices,sortedPrices,"Items are not sorted !");
    }
    public  void addProduct(String product)
    {
        try {
            client.getWebDriver().findElement(By.xpath(String.format(PRODUCT_ADD_BUTTON,product))).click();
            client.getWebDriverWait()
                    .until(ExpectedConditions.presenceOfElementLocated(PRODUCT_REMOVE_BUTTON));
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
    public  void openCart()
    {
        try {
            client.getWebDriver().findElement(SHOPPING_CART_LINK).click();
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}
