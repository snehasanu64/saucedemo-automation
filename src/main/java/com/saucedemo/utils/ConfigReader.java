package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Reads config.properties. A -Dkey=value on the command line wins over the file. */
public class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        // Load the file once, when the class is first used
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String fromCommandLine = System.getProperty(key);
        // Ignore empty values so an unset -D flag never hides the file value
        if (fromCommandLine != null && !fromCommandLine.isBlank()) {
            return fromCommandLine.trim();
        }
        String fromFile = PROPS.getProperty(key);
        if (fromFile == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return fromFile.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
