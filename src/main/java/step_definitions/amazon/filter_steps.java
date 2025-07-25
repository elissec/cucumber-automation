package step_definitions.amazon;

import PageObject.PageObjectModel;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import webdriver.DriverController;

public class filter_steps extends PageObjectModel{

    public filter_steps(DriverController objDriverController) {
        super(objDriverController);
    }

    @When("I search for {string}")
    public void iSearchFor(String arg0) {
        objAmazonPage.searchForItem(arg0);
    }

    @And("I select Sort by {string}")
    public void iSelectSortBy(String arg0) {
        objAmazonPage.sortSearchResults(arg0);
    }

    @Then("I should see order of results based on {string}")
    public void iShouldSeeOrderOfResultsBasedOn(String arg0) {
        objAmazonPage.verifySortedResults(arg0);
    }
}
