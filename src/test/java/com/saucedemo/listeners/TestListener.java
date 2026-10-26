package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.saucedemo.utils.DriverFactory;
import com.saucedemo.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

/** Writes target/reports/extent-report.html and adds a screenshot when a test fails. */
public class TestListener implements ITestListener {

    private static final ExtentReports EXTENT = createReport();
    // One ExtentTest per thread, so parallel tests do not mix up their logs
    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    private static ExtentReports createReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/reports/extent-report.html");
        spark.config().setReportName("SauceDemo Automation Report");
        spark.config().setDocumentTitle("SauceDemo Test Report");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));
        return extent;
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Test name = description (e.g. "TC_LOGIN_01: ...") plus data row when data-driven
        String name = result.getMethod().getDescription();
        if (name == null || name.isEmpty()) {
            name = result.getMethod().getMethodName();
        }
        if (result.getParameters().length > 0) {
            name += " " + Arrays.toString(result.getParameters());
        }
        CURRENT.set(EXTENT.createTest(name));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        CURRENT.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = CURRENT.get();
        test.fail(result.getThrowable());
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            // Save a PNG file and also embed the picture inside the report
            ScreenshotUtil.saveScreenshot(driver, result.getMethod().getMethodName());
            test.fail("Screenshot at the moment of failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(ScreenshotUtil.getBase64(driver)).build());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // TestNG also reports a failed attempt that will be retried as "skipped"
        ExtentTest test = CURRENT.get();
        if (test != null) {
            test.skip(result.getThrowable() != null ? result.getThrowable().toString() : "Test skipped");
            WebDriver driver = DriverFactory.getDriver();
            if (driver != null && result.getThrowable() != null) {
                ScreenshotUtil.saveScreenshot(driver, result.getMethod().getMethodName());
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        // Writes the HTML file
        EXTENT.flush();
    }
}
