package webdriver;

import helpers.ConfigReader;
import helpers.Utilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.Assert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DriverController {

    private final ConfigReader objConfigReader = new ConfigReader();
    private final Utilities objUtilities = new Utilities();
    private WebDriver driver;


    public WebDriver setWebDriver() {

        String strHubPath = "";
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);

        if(System.getProperty("HUB_URL") == null) {
            strHubPath = objConfigReader.readGlobalConfig("HUB_URL");
        } else {
            strHubPath = System.getProperty("HUB_URL");
        }

        if(System.getProperty("BROWSER") == null) {
            System.setProperty("BROWSER", "chrome");
        }

        if(driver == null) {
            switch(System.getProperty("BROWSER").toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
//                    if (!Utilities.getOS().contains("win") && !Objects.equals(System.getProperty("HEADLESS"), "y")) {
//                        chromeOptions.addArguments("--headless");
//                    }
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("start-maximized");
                    chromeOptions.addArguments("enable-automation");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--disable-infobars");
                    chromeOptions.addArguments("--disable-browser-side-navigation");
                    chromeOptions.setExperimentalOption("prefs", prefs);
                    chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    try {
                        driver = new RemoteWebDriver(new URL(strHubPath), chromeOptions);
                    } catch (MalformedURLException e) {
                        objUtilities.logError("Invalid hub URL!");
                    }
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                    if (!Utilities.getOS().contains("win")) {
                        firefoxOptions.addArguments("--headless");
                    }
                    firefoxOptions.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                    try {
                        driver = new RemoteWebDriver(new URL(strHubPath), firefoxOptions);
                    } catch (MalformedURLException e) {
                        objUtilities.logError("Invalid hub URL!");
                    }
                    break;
                case "safari":
                    SafariOptions safariOptions = new SafariOptions();
                    try {
                        driver = new RemoteWebDriver(new URL(strHubPath), safariOptions);
                    } catch (MalformedURLException e) {
                        objUtilities.logError("Invalid hub URL!");
                    }
                    break;
                default:
                    objUtilities.assertFail("Unrecognized browser provided!");
                    break;
            }
        } else {
            objUtilities.logInfo("Using existing session.");
        }
        return driver;
    }

    public WebDriver getDriver(){
        return driver;
    }

    private void killSafariProcess() {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","ps ux | grep -i app/Contents/MacOs/Safari | grep -v grep | awk '{print $2}' | xargs kill -9"});
        } catch (IOException ex) {
            objUtilities.logError("An error was encountered upon terminating Safari driver: "+ex.getMessage());
        }
    }

}
