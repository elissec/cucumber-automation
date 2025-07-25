
Feature: Amazon Search Functionality

  Background:
    Given I am on the Amazon homepage

  @Search @qa-1
  Scenario Outline: Verify search results for <Keyword> are displayed correctly
    When I search for "<Keyword>" in the search bar
    Then I should see search results contain "<Keyword>"
    Examples:
      | Keyword          |
      | iphone 15 pro max           |
#      | android           |
#      | windows           |


  @SortResults
  Scenario Outline: Verify sort feature when using <criteria>
    When I search for "iphone"
    And I select Sort by "<criteria>"
    Then I should see order of results based on "<criteria>"
    Examples:
      | criteria          |
      | Low to High       |
      | High to Low       |
