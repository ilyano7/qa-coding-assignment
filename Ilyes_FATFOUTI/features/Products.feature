@all
Feature: Prodcuts

  Scenario: Sort the items (Lowest Price sort)
    Given I navigate to the demo site
    When I set the user and the password
    And I should be redirected to the products page
    And I sort the items with "Price (low to high)"
    Then The items should be sorted

