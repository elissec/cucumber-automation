package step_definitions.amazon;

import PageObject.PageObjectModel;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import webdriver.DriverController;

public class search_steps extends PageObjectModel {

    public search_steps(DriverController objDriverController) {
        super(objDriverController);
    }

    @When("I search for {string} in the search bar")
    public void iSearchForInTheSearchBar(String arg0) {
        objAmazonPage.searchForItem(arg0);
    }

    @Then("I should see search results contain {string}")
    public void iShouldSeeSearchResultsContains(String arg0) {
        objAmazonPage.verifySearchResults(arg0);
    }
}
