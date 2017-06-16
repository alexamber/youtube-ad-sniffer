package com.youtubesniffer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.youtubesniffer.pageobject.YouTubeHomePage;

import org.openqa.selenium.WebDriver;

import static java.lang.String.format;

public class YouTubeSniffer {

    private final String youtubeHost;
    private final String youtubeSearchPhrase;
    private final int numberOfVideosToAnalyze;
    private boolean adblock;
    private WebDriver driver;
    private YouTubeHomePage youTubeHomePage;

    public YouTubeSniffer(final WebDriver driver, final String youtubeHost, final boolean adblock,
            final String youtubeSearchPhrase, final int numberOfVideosToAnalyze) {
        this.driver = driver;
        this.youtubeHost = youtubeHost;
        this.adblock = adblock;
        this.youtubeSearchPhrase = youtubeSearchPhrase;
        this.numberOfVideosToAnalyze = numberOfVideosToAnalyze;
        youTubeHomePage = new YouTubeHomePage(driver);
    }

    public void sniff() {
        try {
            System.out.println("Starting AD analysis. adblock=" + adblock);
            openAndSearchYouTube(youtubeSearchPhrase);
            closeAdBlockTabIfUsed();
            List<String> videoIdsToPlay = selectFirstNVideos(numberOfVideosToAnalyze);
            Map<String, Boolean> adAppearanceResultMap = playEachVideoAndCheckIfAdAppeared(videoIdsToPlay);
            writeResultToFile(adAppearanceResultMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to operate. Stopping.", e);
        } finally {
            driver.quit();
        }
    }

    private List<String> selectFirstNVideos(final int numberOfVideosToAnalyze) {
        List<String> videoIds;
        List<String> videoResultIds = youTubeHomePage.getVideoResultIds();
        int numberOfVideosOnPage = videoResultIds.size();
        System.out.println(format("%s videos found on the page", numberOfVideosOnPage));
        if (numberOfVideosOnPage < numberOfVideosToAnalyze) {
            System.out.println(
                    "Number of requested videos is bigger than number of videos found on the page. Analysing only videos that were found.");
            videoIds = videoResultIds.subList(0, numberOfVideosOnPage);
        } else {
            System.out.println(format("Analysing first %s videos", numberOfVideosToAnalyze));
            videoIds = videoResultIds.subList(0, numberOfVideosToAnalyze);
        }
        System.out.println("Found videos\n" + String.join("\n", videoIds));
        return videoIds;
    }

    private void openAndSearchYouTube(final String searchPhrase) {
        driver.get(youtubeHost + "/results?search_query=" + searchPhrase.replace(" ", "+"));
    }

    private void closeAdBlockTabIfUsed() {
        if (Boolean.getBoolean("adblock")) {
            List<String> tabs = new ArrayList(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));
            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
    }

    private Map<String, Boolean> playEachVideoAndCheckIfAdAppeared(final List<String> videoIdsToPlay) {
        Map<String, Boolean> videoAdMap = new HashMap<>();
        for (String id : videoIdsToPlay) {
            driver.get(youtubeHost + "/watch?v=" + id);
            boolean adAppeared = youTubeHomePage.checkIfAdAppeared();
            System.out.println(format("Video id: %s AD=%s", id, adAppeared));
            videoAdMap.put(id, adAppeared);
        }
        return videoAdMap;
    }

    private void writeResultToFile(final Map<String, Boolean> resultMap) {
        String l = "|";
        String fileName = "video-ad-status_adblock-" + adblock + ".txt";
        String content = resultMap.entrySet().stream().map(e -> l + e.getKey() + l + e.getValue() + l)
                .collect(Collectors.joining("\n"));
        try {
            Files.write(Paths.get(fileName), content.getBytes());
            System.out.println(format("Successfully wrote report to: %s", fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write resulting report", e);
        }
    }

}
