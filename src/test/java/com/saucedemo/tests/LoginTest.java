package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginTest extends BaseTest {

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return JsonDataReader.read("login-negative.json");
    }

    @Test(description = "TC_LOGIN_01: Valid user can log in and sees the Products page")
    public void validLogin() {
        ProductsPage products = loginAsValidUser();
        Assert.assertEquals(products.getTitle(), "Products", "Products page title is wrong after login");
        Assert.assertTrue(products.getCurrentUrl().contains("inventory"), "URL should contain 'inventory' after login");
    }

    @Test(dataProvider = "invalidLogins", description = "TC_LOGIN_02: Invalid login shows the correct error")
    public void invalidLogin(Map<String, String> data) {
        LoginPage login = loginPage();
        login.enterCredentialsAndSubmit(data.get("username"), data.get("password"));
        Assert.assertEquals(login.getErrorMessage(), data.get("expectedError"),
                "Wrong error message for scenario: " + data.get("scenario"));
    }

    @Test(description = "TC_LOGIN_03: Locked-out user cannot log in")
    public void lockedOutUser() {
        LoginPage login = loginPage();
        login.enterCredentialsAndSubmit(ConfigReader.get("locked.username"), ConfigReader.get("valid.password"));
        Assert.assertEquals(login.getErrorMessage(), "Epic sadface: Sorry, this user has been locked out.",
                "Locked-out user error message is wrong");
    }

    @Test(description = "TC_LOGIN_04: Logged-in user can log out")
    public void logout() {
        ProductsPage products = loginAsValidUser();
        LoginPage login = products.logout();
        Assert.assertTrue(login.isLoginButtonDisplayed(), "Login button should be visible after logout");
    }
}
