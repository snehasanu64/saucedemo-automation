package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class CheckoutTest extends BaseTest {

    @DataProvider(name = "invalidCheckoutDetails")
    public Object[][] invalidCheckoutDetails() {
        return JsonDataReader.read("checkout-negative.json");
    }

    /** Logs in, adds a product and goes to the checkout form. */
    private CheckoutPage goToCheckoutForm() {
        ProductsPage products = loginAsValidUser();
        products.addToCart("Sauce Labs Backpack");
        CartPage cart = products.openCart();
        return cart.proceedToCheckout();
    }

    @Test(dataProvider = "invalidCheckoutDetails", description = "TC_CHECKOUT_01: Missing details show an error")
    public void missingDetailsShowError(Map<String, String> data) {
        CheckoutPage checkout = goToCheckoutForm();
        checkout.fillDetailsAndContinue(data.get("firstName"), data.get("lastName"), data.get("postalCode"));
        Assert.assertEquals(checkout.getErrorMessage(), data.get("expectedError"),
                "Wrong error message for scenario: " + data.get("scenario"));
    }

    @Test(description = "TC_CHECKOUT_02: Complete order shows the thank-you message")
    public void completeOrder() {
        CheckoutPage checkout = goToCheckoutForm();
        checkout.fillDetailsAndContinue("John", "Doe", "12345");
        Assert.assertEquals(checkout.getTitle(), "Checkout: Overview", "Should reach the overview step");
        checkout.finishOrder();
        Assert.assertEquals(checkout.getConfirmationMessage(), "Thank you for your order!",
                "Order confirmation message is wrong");
    }
}
