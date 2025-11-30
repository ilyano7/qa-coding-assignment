package test.automation.pages;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;
import test.automation.utils.TestProperties;

import java.util.Arrays;
import java.util.List;

public class Checkout extends AbstractPageObject{


    private static final By CHECKOUT_TITLE =By.xpath("//span[text()='Checkout: Your Information']");
    private static final By CHECKOUT_FIRST_NAME =By.id("first-name");
    private static final By CHECKOUT_LAST_NAME =By.id("last-name");
    private static final By CHECKOUT_POSTAL_CODE =By.id("postal-code");
    private static final By CHECKOUT_CONTINUE_BUTTON =By.id("continue");
    private static final By CHECKOUT_ERROR=By.xpath("//h3[@data-test='error']");
    private static final By CHECKOUT_TOTAL=By.xpath("//div[@data-test='total-label']");
    private static final By CHECKOUT_FINISH_BUTTON =By.id("finish");
    private static final By CHECKOUT_COMPLETE=By.xpath("//h2[@data-test='complete-header']");
    private static final By CHECKOUT_SHOPPING_CART_ITEMS=By.xpath("//span[@data-test='shopping-cart-badge']");

    protected Checkout(SeleniumClient client, TestContext context) {
        super(client, context);
    }
    public static Checkout reachAndCreate(SeleniumClient client, TestContext context) {
        Checkout page = new Checkout(client, context);
        page.validate();
        return page;
    }
    @Override
    protected List<By> getMandatoryElements() {
        return ImmutableList.of(CHECKOUT_TITLE);
    }

    public void checkRequiredInput(String input)
    {
        try {
            WebElement error=null;
            switch (input) {
                case "firstName":
                    client.getWebDriverWait()
                            .until(ExpectedConditions.presenceOfElementLocated(CHECKOUT_CONTINUE_BUTTON)).click();
                    error= client.getWebDriverWait()
                            .until(ExpectedConditions.presenceOfElementLocated(CHECKOUT_ERROR));
                    Assert.assertEquals(error.getText(),"Error: First Name is required","first name is not required !");
                break;
                case "lastName":
                    setInput("firstname","test");
                    client.getWebDriverWait()
                            .until(ExpectedConditions.presenceOfElementLocated(CHECKOUT_CONTINUE_BUTTON)).click();
                    error= client.getWebDriverWait()
                            .until(ExpectedConditions.presenceOfElementLocated(CHECKOUT_ERROR));
                    Assert.assertEquals(error.getText(),"Error: Last Name is required","last name is not required !");
                break;
                case "codePostal":
                    setInput("firstname","test");
                    setInput("lastname","test");
                    client.getWebDriverWait()
                            .until(ExpectedConditions.presenceOfElementLocated(CHECKOUT_CONTINUE_BUTTON)).click();
                    error= client.getWebDriverWait()
                            .until(ExpectedConditions.presenceOfElementLocated(CHECKOUT_ERROR));
                    Assert.assertEquals(error.getText(),"Error: Postal Code is required","post code is not required !");
                    break;

            }
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
    public void setInput(String field, String value) {
        By locator;

        switch (field.toLowerCase()) {
            case "firstname":
                locator = CHECKOUT_FIRST_NAME;
                break;
            case "lastname":
                locator = CHECKOUT_LAST_NAME;
                break;
            case "postalcode":
                locator = CHECKOUT_POSTAL_CODE;
                break;
            default:
                throw new RuntimeException("Unknown field: " + field);
        }

        client.getWebDriverWait()
                .until(ExpectedConditions.presenceOfElementLocated(locator))
                .sendKeys(value);
    }
    public void clickOnContinue() {
        try {
            client.getWebDriver().findElement(CHECKOUT_CONTINUE_BUTTON).click();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    public double calculateItemTotal() {
            List<WebElement> items = client.getWebDriver().findElements(By.cssSelector("div.cart_item"));

            double total = 0.0;

            for (WebElement item : items) {

                int qty = Integer.parseInt(
                        item.findElement(By.cssSelector(".cart_quantity")).getText().trim()
                );

                double price = Double.parseDouble(
                        item.findElement(By.cssSelector(".inventory_item_price"))
                                .getText()
                                .replace("$", "")
                                .trim()
                );

                total += price * qty;
            }
        return total;
    }
    public double calculateTax(double itemTotal) {
        return itemTotal * 0.08;
    }

    public double calculateFinalTotal() {
        double itemTotal = calculateItemTotal();
        double tax = calculateTax(itemTotal);
        return itemTotal + tax;
    }
    public double readDisplayedFinalTotal() {
        String text = client.getWebDriver()
                .findElement(CHECKOUT_TOTAL)
                .getText()
                .replace("Total: $", "")
                .trim();

        return Double.parseDouble(text);
    }
    public void assertTotal() {

        double expected = calculateFinalTotal();
        double displayed = readDisplayedFinalTotal();

        Assert.assertEquals(displayed, expected, 0.01,
                "❌ the total is not correct !");
        client.getWebDriver().findElement(CHECKOUT_FINISH_BUTTON).click();
        Assert.assertEquals(client.getWebDriver().findElement(CHECKOUT_COMPLETE).getText(),"Thank you for your order!","Wrong Message checkout !");

    }
    public void checkEmptyCart()
    {
        Assert.assertEquals(client.getWebDriver().findElements(CHECKOUT_SHOPPING_CART_ITEMS).size(),0,"Cart is not empty !!");
    }
}
