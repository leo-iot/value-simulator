Feature: To test the config-resource

  Background:
    * url baseUrl

  Scenario: Add a new room
    Given path 'addRoom/testRoom'
    When method POST
    Then status 201

  Scenario: delete the new room
    Given path 'deleteRoom/testRoom'
    When method POST
    Then status 200

  Scenario: update the new room
    Given path 'updateRoom/testRoom'
    And  params { newName: 'newTestRoom'}
    When method PUT
    Then status 200
