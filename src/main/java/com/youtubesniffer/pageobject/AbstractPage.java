package com.youtubesniffer.pageobject;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class AbstractPage {
    private WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isElementVisible(final WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (ElementNotVisibleException e) {
            return false;
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }
}
