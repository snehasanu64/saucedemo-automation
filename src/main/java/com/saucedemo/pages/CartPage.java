package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public int getItemCount() {
        // Wait for the page title so we count after the cart page has loaded
        getTitle();
        return driver.findElements(cartItems).size();
    }

    public List<String> getItemNames() {
        getTitle();
        return driver.findElements(itemNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void removeItem(String productName) {
        By removeButton = By.xpath("//div[contains(@class,'cart_item')][.//div[contains(@class,'inventory_item_name') "
                + "and normalize-space(.)='" + productName + "']]//button");
        click(removeButton);
        // Wait until that row has disappeared
        wait.until(ExpectedConditions.invisibilityOfElementLocated(removeButton));
    }

    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }

    public ProductsPage continueShopping() {
        click(continueShoppingButton);
        return new ProductsPage(driver);
    }
}
