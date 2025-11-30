@all
Feature: Cart

  Scenario: Add products to the shopping cart
    Given I navigate to the demo site
    When I set the user and the password
    And I should be redirected to the products page
    And I add "onesie" to the shopping cart
    And I add "light" to the shopping cart
    And I open the shopping cart
    Then I should be redirected to the shopping cart page


  Scenario Outline: verify items in the shopping cart
    Given I navigate to the demo site
    When I set the user and the password
    And I should be redirected to the products page
    And I add "onesie" to the shopping cart
    And I add "light" to the shopping cart
    And I open the shopping cart
    Then I should be redirected to the shopping cart page
    And I check that the "<ListOfItems>" that I added in the cart are correct
    Examples:
      |ListOfItems                                         |
      | Sauce Labs Onesie,Sauce Labs Bike Light            |