package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigReader {

    private final GlobalVariables objGlobalVariables = new GlobalVariables();
    private final Utilities objUtilities = new Utilities();

    private ConcurrentHashMap<String, String> globalConfigProperties;
    private ConcurrentHashMap<String, String> project1ConfigProperties;

    public String readGlobalConfig(String strKey) {
        Properties prop = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    System.getProperty("user.dir") + "/src/main/resources/config.properties");
            prop.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            objUtilities.assertFail("Config file not found!");
        } catch (IOException e) {
            objUtilities.assertFail("Key entry not found!");
        }
        return prop.getProperty(strKey);
    }

    public String project1Config(String strKey) {
        Properties prop = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    System.getProperty("user.dir") + "/src/main/resources/project_1/system_1/system_1.properties");
            prop.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            objUtilities.assertFail("Config file not found!");
        } catch (IOException e) {
            objUtilities.assertFail("Key entry not found!");
        }
        return prop.getProperty(strKey);
    }

//    public String readGlobalConfig(String strKey) { return getGlobalProperties().get(strKey); }
//    public String project1Config(String strKey) { return getProject1Properties().get(strKey); }

    // -----------------------------------------------
    // Stores all the values in the config.properties
    // file in a map
    // -----------------------------------------------
    synchronized ConcurrentHashMap<String, String> getGlobalProperties() {
        try {
            if (globalConfigProperties == null) {
                globalConfigProperties = new ConcurrentHashMap<>();
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream(
                        System.getProperty("user.dir") + "/src/main/resources/config.properties");
                properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
                for (Object entryKey : properties.keySet()) {
                    globalConfigProperties.put(entryKey.toString(), properties.getProperty(entryKey.toString()));
                }
            }
        } catch (IOException e) {
            objUtilities.assertFail("configProperties == null : " + e.toString());
        }
        return globalConfigProperties;
    }

    synchronized ConcurrentHashMap<String, String> getProject1Properties() {
        try {
            if (project1ConfigProperties == null) {
                project1ConfigProperties = new ConcurrentHashMap<>();
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream(
                        System.getProperty("user.dir") + "/src/main/resources/Project1/project1.properties");
                properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
                for (Object entryKey : properties.keySet()) {
                    project1ConfigProperties.put(entryKey.toString(), properties.getProperty(entryKey.toString()));
                }
            }
        } catch (IOException e) {
            objUtilities.assertFail("configProperties == null : " + e.toString());
        }
        return project1ConfigProperties;
    }
}
