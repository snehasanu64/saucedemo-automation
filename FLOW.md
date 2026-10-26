# Working flow

```
mvn test
   │   Maven reads pom.xml and starts Surefire, which reads testng.xml
   ▼
TestNG
   │   loads the listeners (TestListener, RetryTransformer) and finds the @Test methods
   ▼
@BeforeMethod  (BaseTest.setUp)
   │   DriverFactory starts a new Chrome, then the browser opens the URL from config.properties
   ▼
@Test  (e.g. CartTest.addOneProduct)
   │   the test calls Page Object methods: login -> add to cart -> read the badge
   ▼
Assert
   │   TestNG Assert compares expected vs actual
   │      pass -> TestListener.onTestSuccess logs a pass in the report
   │      fail -> TestListener.onTestFailure saves target/screenshots/<test>.png
   │              and embeds the screenshot in the report; RetryAnalyzer re-runs the test once
   ▼
@AfterMethod  (BaseTest.tearDown, alwaysRun = true)
   │   DriverFactory.quitDriver closes the browser, even when the test failed
   ▼
Report
       TestListener.onFinish writes target/reports/extent-report.html
```

With `parallel="methods"` up to three of these loops run side by side. Each thread has its own browser
(`ThreadLocal<WebDriver>` in `DriverFactory`), so the tests do not disturb each other.
