package com.youtubesniffer.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyManager {
    public static void mergeProperties(final String propertyFile) {
        Properties newProps = new Properties();
        Properties propFromFile = new Properties();
        try {
            propFromFile.load(Files.newBufferedReader(Paths.get(propertyFile)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read properties from " + propertyFile, e);
        }
        newProps.putAll(propFromFile);
        newProps.putAll(System.getProperties());
        System.setProperties(newProps);
    }

}
