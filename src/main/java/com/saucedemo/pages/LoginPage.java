package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open(String url) {
        driver.get(url);
    }

    /** Fills the form and clicks Login without checking the result (used for negative tests). */
    public void enterCredentialsAndSubmit(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    /** Successful login: returns the Products page. */
    public ProductsPage loginAs(String username, String password) {
        enterCredentialsAndSubmit(username, password);
        return new ProductsPage(driver);
    }

    /** Text of the red error box shown after a failed login. */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }
}
