package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By pageTitle = By.className("title");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /** Step one: fill in the customer details and press Continue. Empty strings are allowed. */
    public void fillDetailsAndContinue(String firstName, String lastName, String postalCode) {
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(postalCodeField, postalCode);
        click(continueButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /** Title of the current checkout step, e.g. "Checkout: Overview". */
    public String getTitle() {
        return getText(pageTitle);
    }

    /** Step two: press Finish on the overview page. */
    public void finishOrder() {
        click(finishButton);
    }

    public String getConfirmationMessage() {
        return getText(completeHeader);
    }
}
