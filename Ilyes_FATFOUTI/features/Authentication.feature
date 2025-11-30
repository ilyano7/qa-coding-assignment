@all
Feature: Authentication

Scenario: Login to website
Given I navigate to the demo site
When I set the user and the password
Then I should be redirected to the products page