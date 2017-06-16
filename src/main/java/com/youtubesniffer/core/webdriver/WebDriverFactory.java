package com.youtubesniffer.core.webdriver;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class WebDriverFactory {

    private static WebDriverFactory factoryInstance;
    private boolean isInitialized = false;

    private WebDriverFactory() {
    }

    public static WebDriverFactory getInstance() {
        if (null == factoryInstance)
            factoryInstance = new WebDriverFactory();
        return factoryInstance;
    }

    private final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>() {
        protected WebDriver initialValue() {
            isInitialized = true;
            return initDriver(System.getProperty("webdriver.driver"));
        }
    };

    public WebDriver getDriver() {
        return threadDriver.get();
    }

    public boolean isInstantiated() {
        return isInitialized;
    }

    public void reset() {
        isInitialized = false;
        threadDriver.remove();
    }

    private WebDriver initDriver(final String driverName) {
        WebDriver driver;
        switch (driverName) {
        case "chrome":
            driver = new ChromeDriver(withChromeOptions());
            break;
        default:
            throw new RuntimeException(
                    driverName + " driver is not supported. Please check if the right driver was set.");
        }
        return withProperties(driver);
    }

    private WebDriver withProperties(final WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Integer.getInteger("webdriver.implicity.wait", 1000),
                TimeUnit.MILLISECONDS);
        return driver;
    }

    private ChromeOptions withChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        if (Boolean.getBoolean("adblock")) {
            options.addExtensions(new File(System.getProperty("adblock.path")));
        }
        return options;
    }
}
