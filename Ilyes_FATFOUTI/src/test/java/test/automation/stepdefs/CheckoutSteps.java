package test.automation.stepdefs;

import io.cucumber.java.en.And;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;

public class CheckoutSteps {
    TestContext context;
    SeleniumClient client;

    public CheckoutSteps(TestContext context, SeleniumClient client) {
        this.context = context;
        this.client = client;

    }
    @And("The {string} should be required")
    public void checkRequiredInputs(String input)
    {
        context.getCheckoutPage().checkRequiredInput(input);
    }
    @And("I fill {string} with {string}")
    public void setInputs(String field,String value)
    {
        context.getCheckoutPage().setInput(field,value);
    }
    @And("I click the continue button")
    public void clickOnContinue()
    {
        context.getCheckoutPage().clickOnContinue();
    }
    @And("I check the total price")
    public void checkTotalPice()
    {
        context.getCheckoutPage().assertTotal();
    }
    @And("The cart is set back to empty")
    public void checkEmptyCart()
    {
        context.getCheckoutPage().checkEmptyCart();
    }

}
