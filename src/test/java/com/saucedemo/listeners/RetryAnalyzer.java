package com.saucedemo.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** Re-runs a failed test one more time (helps with flaky network or timing problems). */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 1;
    // A new RetryAnalyzer object is created per test, so this counter is per test
    private int attempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempt < MAX_RETRIES) {
            attempt++;
            return true;
        }
        return false;
    }
}
