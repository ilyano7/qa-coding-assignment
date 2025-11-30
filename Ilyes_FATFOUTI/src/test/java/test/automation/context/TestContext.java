package test.automation.context;

import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.automation.pages.Cart;
import test.automation.pages.Checkout;
import test.automation.pages.Login;
import test.automation.pages.Products;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestContext {

	protected static final Logger LOGGER = LogManager.getLogger(TestContext.class.getSimpleName());

	protected Map<ContextKey, Object> context = Collections.synchronizedMap(new HashMap<>());



	public Scenario getScenario() {
		return (Scenario) context.get(ContextKey.SCENARIO);
	}

	public void setScenario(Scenario testScenario) {
		context.put(ContextKey.SCENARIO, testScenario);
	}
	public Login getLoginPage()
	{
		return  (Login) context.get(ContextKey.LOGIN_PAGE);
	}
	public void setLoginPage(Login login)
	{
		context.put(ContextKey.LOGIN_PAGE,login);
	}
	public Products getProductsPage()
	{
		return  (Products) context.get(ContextKey.PRODUCTS_PAGE);
	}
	public void setProductsPage(Products products)
	{
		context.put(ContextKey.PRODUCTS_PAGE,products);
	}

	public Cart getCartPage()
	{
		return  (Cart) context.get(ContextKey.CART_PAGE);
	}
	public void setCartPage(Cart cart)
	{
		context.put(ContextKey.CART_PAGE,cart);
	}
	public Checkout getCheckoutPage()
	{
		return  (Checkout) context.get(ContextKey.CHECKOUT_PAGE);
	}
	public void setCheckoutPage(Checkout checkout)
	{
		context.put(ContextKey.CHECKOUT_PAGE,checkout);
	}



	private enum ContextKey {
		SCENARIO,
		LOGIN_PAGE,
		PRODUCTS_PAGE,
		CART_PAGE,
		CHECKOUT_PAGE
	}
}
