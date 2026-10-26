# SauceDemo Selenium Automation

Automated UI tests for the practice shop https://www.saucedemo.com, written in Java with Selenium and TestNG.
It covers **login, products, cart, checkout and logout** (23 test runs). It is built to be simple and a good
starting point if you are learning test automation.

## Tech stack

| Purpose | Tool |
|---|---|
| Language | Java 17 |
| Browser automation | Selenium WebDriver 4.25.0 (Selenium Manager downloads the driver for you) |
| Test runner / assertions | TestNG 7.10.2 |
| Build tool | Maven 3.9+ |
| Design pattern | Page Object Model |
| Test data | JSON files read with Gson 2.11.0 |
| Reporting | Extent Reports 5.1.2 |
| Logging | slf4j-simple |
| CI/CD | GitHub Actions |

## Folder structure

```
automation/
├── pom.xml                       Maven build file (dependencies, surefire)
├── testng.xml                    Which test classes run, parallel setting, listeners
├── .github/workflows/tests.yml   GitHub Actions pipeline
└── src
    ├── main
    │   ├── java/com/saucedemo
    │   │   ├── pages/            Page Objects (all locators live here)
    │   │   └── utils/            ConfigReader, DriverFactory, ScreenshotUtil, JsonDataReader
    │   └── resources/config.properties   URL, browser, headless, timeout, credentials
    └── test
        ├── java/com/saucedemo
        │   ├── base/BaseTest.java        Opens/closes the browser for every test
        │   ├── listeners/                TestListener (report + screenshots), RetryAnalyzer
        │   └── tests/                    LoginTest, ProductsTest, CartTest, CheckoutTest
        └── resources/testdata/           login-negative.json, checkout-negative.json
```

## How it works (in simple words)

1. `testng.xml` tells TestNG which test classes to run.
2. Before each test, `BaseTest` starts a **fresh browser** and opens the website.
3. The test talks to **Page Objects** (`LoginPage`, `ProductsPage`...). A page object knows the locators and
   actions of one page, so tests read like plain steps: `loginAsValidUser()`, `addToCart(...)`.
4. The test checks the result with `Assert`. If it fails, `TestListener` takes a screenshot.
5. After each test, `BaseTest` closes the browser (even when the test failed).
6. `TestListener` writes the HTML report at the end.

Rules the project follows: no `Thread.sleep` (only explicit `WebDriverWait`), no locators in tests, and the URL,
browser, headless flag, timeout and credentials are kept in `config.properties`.

## Setup

1. Install **JDK 17**, **Maven 3.9+** and **Google Chrome**.
2. Check them: `java -version` and `mvn -v`.
3. Clone the project and open the folder in IntelliJ IDEA or VS Code.
4. Download dependencies and compile: `mvn -q test-compile`.

## Run commands

| What | Command |
|---|---|
| Normal (browser window visible) | `mvn clean test` |
| Headless (no window, used in CI) | `mvn clean test -Dheadless=true` |
| Firefox | `mvn clean test -Dbrowser=firefox` |
| Edge | `mvn clean test -Dbrowser=edge` |
| One class only | `mvn clean test -Dtest=CartTest` |

Any value in `config.properties` can be overridden like this with `-Dkey=value`.

## Reports

- HTML report: `target/reports/extent-report.html` (open it in a browser)
- Failure screenshots: `target/screenshots/` (also embedded in the HTML report)
- TestNG/Surefire results: `target/surefire-reports/`
- On GitHub Actions, download the `extent-report` and `screenshots` artifacts from the workflow run.

## Test cases

| ID | Class | Description | Runs |
|---|---|---|---|
| TC_LOGIN_01 | LoginTest | Valid login shows the Products page | 1 |
| TC_LOGIN_02 | LoginTest | Invalid logins show the right error (wrong password, unknown user, empty username, empty password, both empty) – data from JSON | 5 |
| TC_LOGIN_03 | LoginTest | Locked-out user cannot log in | 1 |
| TC_LOGIN_04 | LoginTest | Logout returns to the login page | 1 |
| TC_PROD_01 | ProductsTest | Six products are shown | 1 |
| TC_PROD_02 | ProductsTest | Sort price low to high | 1 |
| TC_PROD_03 | ProductsTest | Sort price high to low | 1 |
| TC_PROD_04 | ProductsTest | Sort name Z to A | 1 |
| TC_CART_01 | CartTest | Empty cart has no badge | 1 |
| TC_CART_02 | CartTest | Add one product, badge shows 1 | 1 |
| TC_CART_03 | CartTest | Add two products, badge shows 2 | 1 |
| TC_CART_04 | CartTest | Remove a product from the products page | 1 |
| TC_CART_05 | CartTest | Cart page lists the added items | 1 |
| TC_CART_06 | CartTest | Remove an item inside the cart page | 1 |
| TC_CHECKOUT_01 | CheckoutTest | Missing first name / last name / postal code / all empty show errors – data from JSON | 4 |
| TC_CHECKOUT_02 | CheckoutTest | Complete order shows "Thank you for your order!" | 1 |
| | | **Total** | **23** |

## Parallel runs and retries

- `testng.xml` runs 3 tests at the same time (`parallel="methods" thread-count="3"`). Each test has its own
  browser stored in a `ThreadLocal`, and `BaseTest` keeps no driver in a field, which is what makes this safe.
- A failed test is retried once (`RetryAnalyzer`). The failed first attempt shows as *skipped* in the TestNG summary.

## Push to GitHub

```bash
git remote add origin https://github.com/<your-user>/<your-repo>.git
git branch -M main
git push -u origin main
```

After the push, the **Actions** tab runs `.github/workflows/tests.yml` automatically.
