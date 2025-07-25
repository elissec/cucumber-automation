package PageObject;

import helpers.SeleniumMethods;
import org.testng.Assert;
import webdriver.DriverController;

public class CommonPage extends SeleniumMethods {
    private final SeleniumMethods objSeleniumMethods;

    public CommonPage(DriverController objDriverController) {
        super(objDriverController);
        objSeleniumMethods = new SeleniumMethods(objDriverController);
    }

    public void goToUrlAndVerify(String url) {
        objSeleniumMethods.goToUrl(url);
        Assert.assertTrue(objSeleniumMethods.getCurrentUrl().contains(url));
    }
}
