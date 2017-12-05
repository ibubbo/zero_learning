package net.imain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;
import java.util.Properties;

/**
 * reader file
 *
 * @author: uncle
 * @apdateTime: 2017-11-21 12:49
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties properties;

    static {
        String fileName = "mmall.properties";
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(
                    PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "utf-8")
            );
        } catch (Exception e) {
            logger.error("The configuration file reads exception: {}", e);
        }
    }

    /**
     * Gain key
     *
     * @param key key
     * @return {value}
     */
    public static String getProperties(String key) {
        return getProperties(key, null);
    }

    /**
     * Gain key, It has a default value
     *
     * @param key key
     * @param defaultValue value
     * @return value
     */
    public static String getProperties(String key, String defaultValue) {
        return Optional.of(properties)
                .map(p -> p.getProperty(key.trim()))
                .orElse(defaultValue);
    }
}
