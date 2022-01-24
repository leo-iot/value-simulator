Feature: To test the config-resource

  Background:
    * url baseUrl

  Scenario: Get with valid dates should work
    Given path 'invoices/statistics'
    And  params { s: '2019-12-12T20:11:53.917745', e: '2021-12-14T20:11:53.917745' }
    When method GET
    Then status 200
    And match response == "Number of customers: 2; Products sold: 3; Avg product price: 27.97"

  Scenario: Get with wrong dates (end before start) should return 0 customers work
    Given path 'invoices/statistics'
    And  params { s: '2021-12-12T20:11:53.917745', e: '2019-12-14T20:11:53.917745' }
    When method GET
    Then status 200
    And match response == "Number of customers: 0; Products sold: 0; Avg product price: 0.00"