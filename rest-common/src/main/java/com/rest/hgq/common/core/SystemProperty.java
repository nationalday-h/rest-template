package com.rest.hgq.common.core;

import com.rest.hgq.common.property.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class SystemProperty {
    private static Logger logger = LoggerFactory.getLogger(SystemProperty.class);

    private static Map<String, String> herenProperty;
    private static Map<String, String> systemProperty;
    private static final String DEFAULT_CONFIG_FILENAME = "rest.properties";
    private static final String HEREN_HOME = "HEREN_HOME";

    public static Map<String, String> getHerenProperty() {
        return herenProperty;
    }

    public SystemProperty() {
        herenProperty = new HashMap();
        systemProperty = new HashMap();

    }

    static {
        try {
            reloadAll();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private static void reloadAll() {
        if (herenProperty == null) herenProperty = new HashMap();
        if (systemProperty == null) systemProperty = new HashMap();
        strageSystemProperty();
        strageHerenProperty();
    }

    private static void strageSystemProperty() {
        try {
            systemProperty = System.getenv();
        } catch (Exception e) {
            logger.warn("systemProperty is wrong");
        }
    }

    private static void strageHerenProperty() {
        Map map = new HashMap();
        Properties p = new Properties();
        String home = getHerenHome();
        if (home.length() <= 0) {
            p = PropertyUtils.loadPropertiesFromFile(DEFAULT_CONFIG_FILENAME);
        } else {
            File propertyFile = new File(home, DEFAULT_CONFIG_FILENAME);
            if (propertyFile.exists()) {
                p = PropertyUtils.loadPropertiesFromFile(propertyFile);
            } else {
                p = PropertyUtils.loadPropertiesFromFile(DEFAULT_CONFIG_FILENAME);
            }
        }

        String key;
        String value;
        for (Enumeration enu = p.propertyNames(); enu.hasMoreElements(); map.put(key, value)) {
            key = (String) enu.nextElement();
            value = p.getProperty(key);
        }

        herenProperty = map;
    }


    public static String getHerenProperty(String key) {
        String str = "";
        if (herenProperty != null) {
            if (herenProperty.containsKey(key)) {
                str = herenProperty.get(key);
            }

        }
        return str;
    }

    public static String getSystemPath(String key) {
        String str = "";
        if (systemProperty != null) {
            if (systemProperty.containsKey(key)) {
                str = systemProperty.get(key);
            }
        }
        return str;
    }

    public static String getHerenFile(String fileName) {
        String home = getHerenHome();
        if (home.length() <= 0) {
            return "";
        }
        String filePath = getHerenHome() + File.separator + fileName;
        File file = new File(filePath);
        if(file.exists()){
            return filePath;
        }
        return "";
    }

    public static String getHerenHome() {
        return getSystemPath(HEREN_HOME);
    }

}
