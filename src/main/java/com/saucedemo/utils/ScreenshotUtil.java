package com.saucedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Takes screenshots. PNG files go to target/screenshots. */
public class ScreenshotUtil {

    private static final Path FOLDER = Paths.get("target", "screenshots");

    private ScreenshotUtil() {
    }

    /** Saves a PNG named after the test and returns its path. */
    public static Path saveScreenshot(WebDriver driver, String testName) {
        try {
            Files.createDirectories(FOLDER);
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // Remove characters that are not allowed in file names
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path file = FOLDER.resolve(safeName + "_" + System.currentTimeMillis() + ".png");
            Files.write(file, bytes);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Could not save screenshot", e);
        }
    }

    /** Returns the screenshot as Base64 text (used to embed it in the HTML report). */
    public static String getBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
