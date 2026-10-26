package com.saucedemo.base;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Every test class extends this. Each test gets its own fresh browser.
 * No driver or page is stored in a field: with parallel tests one class object is shared
 * by several threads, so the driver lives in DriverFactory's ThreadLocal instead.
 */
public class BaseTest {

    @BeforeMethod
    public void setUp() {
        // 1. Start a new browser  2. Open the website
        DriverFactory.initDriver();
        loginPage().open(ConfigReader.get("base.url"));
    }

    /** A LoginPage bound to the current thread's browser. */
    protected LoginPage loginPage() {
        return new LoginPage(DriverFactory.getDriver());
    }

    /** Logs in with the valid user from config.properties. */
    protected ProductsPage loginAsValidUser() {
        return loginPage().loginAs(ConfigReader.get("valid.username"), ConfigReader.get("valid.password"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // Runs even if the test failed, so no browser is left open
        DriverFactory.quitDriver();
    }
}
