package step_definitions.amazon;

import PageObject.PageObjectModel;
import helpers.SeleniumMethods;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import webdriver.DriverController;

public class common_steps extends PageObjectModel {

    private final SeleniumMethods objSeleniumMethods;

    public common_steps(DriverController objDriverController) {
        super(objDriverController);
        this.objSeleniumMethods = new SeleniumMethods(objDriverController);
    }

    @Given("I am on the Amazon homepage")
    public void iAmOnTheAmazonHomepage() {
        objCommonPage.goToUrlAndVerify("https://www.amazon.com");
        if(objSeleniumMethods.isElementDisplayed(objAmazonPage.btnContinueShopping))
            objSeleniumMethods.clickElement(objAmazonPage.btnContinueShopping);

    }
}
