# Exercise 1 - Simple Process
The first exercise focuses on setting up the testing environment used in this tutorial and shows how to implement and execute a simple BPMN process.

## Introduction
TODO introduction to the tutorial environment. Link to prerequisites document, how to checkout, what is provided, how to start the DSF instances, how to access the DSF FHIR servers.

## Exercise Tasks
1. Add a log message to the `HelloDic#doExecute` method that contains the recipient organization identifier from the "leading" Task.
1. Register the `HelloDic` class as a singleton bean in the `TutorialConfig` class.
1. Set the `HelloDic` class as the service implementation of the appropriate service task within the `hello-dic.bpmn` process model.
1. Modify the ActivityDefinition for the `highmedorg_helloDic` process to only allow local clients to instantiate the process via a `helloDic` message.

## Solution Verification
1. Execute a maven build of the `dsf-process-tutorial` parent module via:
    ```
    mvn clean install -Pexercise-1
    ```
    Verify that the build was successful and no test failures occurred.
    
2. Verify process execution via the docker test setup:

TODO run tests, start test environment, execute TutorialExampleStarter (set env variables or program args for client-certificate / password)