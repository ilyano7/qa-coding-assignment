package test.automation.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;
import test.automation.pages.Cart;
import test.automation.pages.Checkout;
import test.automation.pages.Products;

public class CartSteps {
    TestContext context;
    SeleniumClient client;
    Checkout checkout;

    public CartSteps(TestContext context,SeleniumClient client) {
        this.context = context;
        this.client = client;

    }
    @And("I check that the {string} that I added in the cart are correct")
    public void checkItems(String items)
    {
        context.getCartPage().checkItems(items);
    }
    @And("I click the checkout button")
    public void checkout()
    {
        context.getCartPage().checkout();
    }
    @Then("I should be redirected to the checkout page")
    public void checkProductsPage()
    {   checkout= Checkout.reachAndCreate(client, context);
        context.setCheckoutPage(checkout);
    }

}
