package com.youtubesniffer;

import java.util.Scanner;

import com.youtubesniffer.core.webdriver.WebDriverFactory;
import com.youtubesniffer.util.PropertyManager;

public class Main {
    private static Scanner scan = new Scanner(System.in, "UTF-8");

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException(
                    "Please specify location of properties file. Should contain:" + "\nwebdriver.driver=<driver-type>"
                            + "\nwebdriver.chrome.driver=<path-to-chromedriver>" + "\nyoutube.base.url=<youtube-url>"
                            + "\nadblock.path=<path-to-adblock-ext>" + "\n#Currently only chrome is supported"
                            + "\n#Optional" + "\nwebdriver.implicity.wait=<timeout-in-ms>");
        }
        String propertyFile = args[0];
        PropertyManager.mergeProperties(propertyFile);
        WebDriverFactory wdf = WebDriverFactory.getInstance();
        String searchPhrase = scanYoutubeSearchPhrase(scan);
        int numberOfVideos = scanNumberOfVideosToAnalyze(scan);
        triggerYoutubeSniffer(wdf, searchPhrase, numberOfVideos);
        resetDriverWithAdBlock(wdf);
        triggerYoutubeSniffer(wdf, searchPhrase, numberOfVideos);
    }

    private static void triggerYoutubeSniffer(final WebDriverFactory wdf, final String searchPhrase,
            final int numberOfVideos) {
        new YouTubeSniffer(wdf.getDriver(), System.getProperty("youtube.base.url"), Boolean.getBoolean("adblock"),
                searchPhrase, numberOfVideos).sniff();
    }

    private static String scanYoutubeSearchPhrase(final Scanner scan) {
        System.out.println("Enter Youtube search phrase:");
        return scan.nextLine();
    }

    private static int scanNumberOfVideosToAnalyze(final Scanner scan) {
        System.out
                .println("Enter number of videos to analyze(Positive integer. Only 1st page is in analysis for now):");
        int requestedNumberOfVideos = scan.nextInt();
        if (requestedNumberOfVideos < 1) {
            throw new RuntimeException("Please run again and enter valid number of videos to analise.");
        }
        return requestedNumberOfVideos;
    }

    private static void resetDriverWithAdBlock(final WebDriverFactory wdf) {
        wdf.reset();
        System.out.println("Turning AdBlock ON");
        System.setProperty("adblock", "true");
    }

}
