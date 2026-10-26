package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By productCards = By.className("inventory_item");
    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By sortDropdown = By.className("product_sort_container");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public boolean isLoaded() {
        return isDisplayed(pageTitle);
    }

    public int getProductCount() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        return driver.findElements(productCards).size();
    }

    public List<String> getProductNames() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
        return driver.findElements(productNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /** Prices as numbers, e.g. "$29.99" becomes 29.99. */
    public List<Double> getProductPrices() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productPrices));
        return driver.findElements(productPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    /** Sort options: "Name (A to Z)", "Name (Z to A)", "Price (low to high)", "Price (high to low)". */
    public void sortBy(String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    // The button sits inside the product card that has the matching name
    private By buttonFor(String productName) {
        return By.xpath("//div[contains(@class,'inventory_item')][.//div[contains(@class,'inventory_item_name') "
                + "and normalize-space(.)='" + productName + "']]//button");
    }

    public void addToCart(String productName) {
        click(buttonFor(productName));
    }

    public void removeFromCart(String productName) {
        click(buttonFor(productName));
    }

    /** Text on the product's button: "Add to cart" or "Remove". */
    public String getButtonText(String productName) {
        return getText(buttonFor(productName));
    }

    /** Badge number, or 0 when the badge is not shown (empty cart). */
    public int getCartCount() {
        if (driver.findElements(cartBadge).isEmpty()) {
            return 0;
        }
        return Integer.parseInt(driver.findElement(cartBadge).getText());
    }

    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(menuButton);
        click(logoutLink);
        return new LoginPage(driver);
    }
}
