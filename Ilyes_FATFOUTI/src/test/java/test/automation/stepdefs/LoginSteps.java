package test.automation.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import test.automation.clients.selenium.SeleniumClient;
import test.automation.context.TestContext;
import test.automation.pages.Login;
import test.automation.pages.Products;

public class LoginSteps {

    TestContext context;
    SeleniumClient client;
    Login login;
    Products products;
    public LoginSteps(TestContext context,SeleniumClient client) {
        this.context = context;
        this.client = client;
    }
    @Given("I navigate to the demo site")
    public void connect() {
        login = Login.reachAndCreate(client, context);
        context.setLoginPage(login);
    }
    @When("I set the user and the password")
    public void setUserAndPassword()
    {
        login.loginToTheDemo();
    }
    @Then("I should be redirected to the products page")
    public void checkProductsPage()
    {   products=Products.reachAndCreate(client, context);
        context.setProductsPage(products);
    }
}
