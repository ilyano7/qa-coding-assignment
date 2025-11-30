@all
Feature: Checkout

  Scenario: verify items in the shopping cart
    Given I navigate to the demo site
    When I set the user and the password
    And I should be redirected to the products page
    And I add "onesie" to the shopping cart
    And I add "light" to the shopping cart
    And I open the shopping cart
    Then I should be redirected to the shopping cart page
    When I click the checkout button
    And I should be redirected to the checkout page
    Then The "firstName" should be required
    And The "LastName" should be required
    And The "codePostal" should be required

    Scenario Outline: verify purchasing item and the total price
      Given I navigate to the demo site
      When I set the user and the password
      And I should be redirected to the products page
      And I add "onesie" to the shopping cart
      And I add "light" to the shopping cart
      And I open the shopping cart
      Then I should be redirected to the shopping cart page
      When I click the checkout button
      And I should be redirected to the checkout page
      And I fill "firstName" with "<firstName>"
      And I fill "lastName" with "<lastName>"
      And I fill "postalCode" with "<code>"
      And I click the continue button
      Then I check that the "<ListOfItems>" that I added in the cart are correct
      And I check the total price
      And The cart is set back to empty
      Examples:
        |firstName  | lastName| code  |ListOfItems                                         |
        |ilyes      |fatfouti |2094   |Sauce Labs Onesie,Sauce Labs Bike Light             |