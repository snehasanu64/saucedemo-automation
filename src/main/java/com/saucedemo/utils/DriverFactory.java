package com.saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

/** Creates and stores one WebDriver per thread (so parallel tests do not clash). */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");

        WebDriver driver;
        switch (browser) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                driver = new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080");
                driver = new EdgeDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080", "--disable-notifications");
                // Turn off Chrome's "save password" and "password leaked" popups
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                options.setExperimentalOption("prefs", prefs);
                driver = new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        driver.manage().window().maximize();
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
