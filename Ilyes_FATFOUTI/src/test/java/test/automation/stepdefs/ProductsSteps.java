package test.automation.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;
import test.automation.pages.Cart;
import test.automation.pages.Login;
import test.automation.pages.Products;

public class ProductsSteps {
    TestContext context;
    SeleniumClient client;
    Cart cart;
    public ProductsSteps(TestContext context,SeleniumClient client) {
        this.context = context;
        this.client = client;

    }
    @And("I sort the items with {string}")
    public void sortItems(String text)
    {
        context.getProductsPage().sortItems(text);
    }
    @Then("The items should be sorted")
    public void checkItems()
    {
        context.getProductsPage().checkSortedItems();
    }
    @And("I add {string} to the shopping cart")
    public void addProductToCart(String product)
    {
        context.getProductsPage().addProduct(product);
    }
    @And("I open the shopping cart")
    public void openCart()
    {
        context.getProductsPage().openCart();
    }
    @Then("I should be redirected to the shopping cart page")
    public void checkProductsPage()
    {   cart=Cart.reachAndCreate(client, context);
        context.setCartPage(cart);
    }
}
