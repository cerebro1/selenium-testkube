package com.example;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.stream.Stream;

public class SeleniumTest {

    private static Stream<? extends Arguments> provideBrowser() {
        String browser = System.getenv("SELENIUM_BROWSER");
        if (browser == null) {
            browser = "chrome";
        }

        return Stream.of(browser).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("provideBrowser")
    public void testWebsite(String browser) {
        WebDriver driver = null;
        Capabilities capabilities = null;

        try {
            if (browser.equalsIgnoreCase("firefox")) {
                capabilities = new FirefoxOptions();
                FirefoxOptions options = (FirefoxOptions) capabilities;
                options.addArguments( "--headless" );
            } else if (browser.equalsIgnoreCase("chrome")) {
                capabilities = new ChromeOptions();
                ChromeOptions options = (ChromeOptions) capabilities;
                options.addArguments("--headless");
            } else if (browser.equalsIgnoreCase("edge")) {
                capabilities = new EdgeOptions();
            } else {
                throw new Exception("Incorrect Browser: " + browser);
            }

            System.out.println("Running test with " + browser);

            // Initialize the Chrome driver with options
            URL url = new URL("http://" + System.getenv("SELENIUM_HOST") + "/wd/hub");
            System.out.println("Connecting to remote webdriver at " + url);
            driver = new RemoteWebDriver(url, capabilities);

            // Maximize the window size
            driver.manage().window().maximize();

            // Navigate to the blog
            driver.get("https://cerebro1.github.io/");

            // Click on the About button
            WebElement about = driver.findElement(By.linkText("About"));
            about.click();
            System.out.println("Finished test");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                System.out.println("endi session..");
                driver.quit();
            }
        }
    }
}
