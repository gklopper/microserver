package com.gu.microserver;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfiguredServlet extends HttpServlet {

    String getProperty(String propertyName) throws IOException {
        Properties configuration = loadConfiguration();
        return configuration.getProperty(propertyName);
    }

    Set<Map.Entry<Object,Object>> getEntries() throws IOException {
        return loadConfiguration().entrySet();
    }

    private Properties loadConfiguration() throws IOException {
        String currentDir = new File("").getAbsolutePath();
        Properties configuration = new Properties();
        String propertyFileLocation = currentDir + File.separator + "config.properties";
        System.out.println("Loading configuration from: " + propertyFileLocation);
        configuration.load(new FileInputStream(propertyFileLocation));
        return configuration;
    }
}
