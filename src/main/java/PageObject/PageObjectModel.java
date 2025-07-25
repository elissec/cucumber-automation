package PageObject;

import webdriver.DriverController;

public class PageObjectModel {

    protected CommonPage objCommonPage;
    protected AmazonPage objAmazonPage;

    public PageObjectModel(DriverController objDriverController) {
        objCommonPage = new CommonPage(objDriverController);
        objAmazonPage = new AmazonPage(objDriverController);
    }
}
