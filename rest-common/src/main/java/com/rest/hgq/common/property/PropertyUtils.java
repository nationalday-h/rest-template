package com.rest.hgq.common.property;

import com.rest.hgq.common.exceptions.SystemException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtils {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);

    public static Properties loadPropertiesFromFile(String propertyFile) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(PropertyUtils.class.getClassLoader().getResourceAsStream(propertyFile), "utf-8");
            Properties properties = new Properties();
            properties.load(reader);
            LOGGER.info(String.format("Load property file %s successfully", propertyFile));
            return properties;
        } catch (Exception e) {
            throw new SystemException(String.format("Failed to load file %s: %s", propertyFile, e.getMessage()), e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static Properties loadPropertiesFromFile(File propertyFile) {
        try {
            Properties properties = new Properties();
            properties.load(FileUtils.openInputStream(propertyFile));
            LOGGER.info(String.format("Load property file %s successfully", propertyFile));
            return properties;
        } catch (Exception e) {
            throw new SystemException(String.format("Failed to load file %s: %s", propertyFile, e.getMessage()), e);
        }
    }
}
