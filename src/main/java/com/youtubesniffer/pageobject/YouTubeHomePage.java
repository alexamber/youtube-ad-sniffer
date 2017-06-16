package com.youtubesniffer.pageobject;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class YouTubeHomePage extends AbstractPage {
    @FindBy(className = "videoAdUi")
    private WebElement videoAdUiBlock;
    @FindBy(xpath = "//div[contains(@class, 'yt-lockup-video')]")
    private List<WebElement> videoBlockResults;

    public YouTubeHomePage(WebDriver driver) {
        super(driver);
    }

    public List<String> getVideoResultIds() {
        return videoBlockResults.stream().map(e -> e.getAttribute("data-context-item-id")).collect(Collectors.toList());
    }

    public boolean checkIfAdAppeared() {
        return isElementVisible(videoAdUiBlock);
    }
}
