package PageObject;

import helpers.SeleniumMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import webdriver.DriverController;

import java.util.List;

public class AmazonPage extends SeleniumMethods {
    private final SeleniumMethods objSeleniumMethods;

    public AmazonPage(DriverController objDriverController) {
        super(objDriverController);
        objSeleniumMethods = new SeleniumMethods(objDriverController);
    }

    private final By txtSearchBox = By.id("twotabsearchtextbox");
    private final By btnSearch = By.id("nav-search-submit-button");
    public final By btnContinueShopping = By.xpath("//span[@class='a-button-inner']/button");
    public final By dismissShippingPopup = By.xpath("//span[contains(text(), 'Dismiss')]");
    private final By productResult = By.xpath("//a[@class='a-link-normal s-line-clamp-2 s-link-style a-text-normal']/h2/span");
    private final By sortDropdown = By.id("s-result-sort-select");
    private final By imgLoading = By.xpath("//div/span[@class='a-spinner a-spinner-medium']");
    private final By searchResultPrices = By.xpath("//span[@class='a-price-whole']");

    public void searchForItem(String item) {
        objSeleniumMethods.enterText(txtSearchBox, item);
        System.out.println("Text Value: " + objSeleniumMethods.getElementValue(txtSearchBox));
        if(objSeleniumMethods.isElementDisplayed(dismissShippingPopup))
            objSeleniumMethods.jseClickElement(dismissShippingPopup);
        Assert.assertTrue(objSeleniumMethods.getElementValue(txtSearchBox).contains(item));
        objSeleniumMethods.clickElement(btnSearch);
    }

    public void verifySearchResults(String item) {
        List<WebElement> lblSearchResults = objSeleniumMethods.getAllElements(productResult);

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < lblSearchResults.size(); i++) {
            softAssert.assertTrue(lblSearchResults.get(i).getText().toLowerCase().contains(item), "Search results #" + (i + 1) + " do not contain the expected item: " + item + " | Actual value: " + lblSearchResults.get(i).getText());
        }
        softAssert.assertAll();
    }

    public void sortSearchResults(String sortOption) {
        By sortOptionDropDown = By.xpath("(//a[contains(@class, 'a-dropdown-link')][contains(text(),'" + sortOption +"')])[1]");

        objSeleniumMethods.jseClickElement(sortDropdown);
        objSeleniumMethods.jseClickElement(sortOptionDropDown);
        Assert.assertTrue(objSeleniumMethods.isElementDisplayed(imgLoading));
        objSeleniumMethods.waitForPageToLoad();
        Assert.assertTrue(objSeleniumMethods.isElementHidden(imgLoading));
    }

    public void verifySortedResults(String sortOption) {

        List<WebElement> lblSearchResults = objSeleniumMethods.getAllElements(productResult);
        List<WebElement> searchResultWithPrices = objSeleniumMethods.getAllElements(searchResultPrices);

        System.out.println("First Page Search Results count: " + lblSearchResults.size());
        System.out.println("Search results that have a price: " + searchResultWithPrices.size());

        SoftAssert softAssert = new SoftAssert();

        // if "Low to High" is selected, the first item should be less than the second item
        if (sortOption.equalsIgnoreCase("Low to High")) {
            for (int i = 0; i < searchResultWithPrices.size(); i++) {
                if(i == searchResultWithPrices.size() - 1) {
                    break;
                }
                System.out.println("Price: " + searchResultWithPrices.get(i).getText());
                int price1 = Integer.parseInt(searchResultWithPrices.get(i).getText().replace(",", "").replace("$", ""));
                int price2 = Integer.parseInt(searchResultWithPrices.get(i+1).getText().replace(",", "").replace("$", ""));
                softAssert.assertTrue(price1 <= price2, "Search result does not match the expected sort option: " + sortOption + " | Actual value: " + price1 + " is not less than or equal to " + price2);
            }
        } else
            for (int i = 0; i < searchResultWithPrices.size(); i++){

                if(i == searchResultWithPrices.size() - 1) {
                    break;
                }
                System.out.println("Price: " + searchResultWithPrices.get(i).getText());
                int price1 = Integer.parseInt(searchResultWithPrices.get(i).getText().replace(",", "").replace("$", ""));
                int price2 = Integer.parseInt(searchResultWithPrices.get(i+1).getText().replace(",", "").replace("$", ""));
                softAssert.assertTrue(price1 >= price2, "Search result does not match the expected sort option: " + sortOption + " | Actual value: " + price1 + " is not greater than or equal to  " + price2);
            }

        softAssert.assertAll();

    }


}
