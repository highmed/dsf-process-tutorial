# Exercise 4 - Exclusive Gateway
Different sequence flows of a process instance execution based on execution variables can be facilitated using exclusive
gateways. We will examine in exercise four how this can be implemented.

## Introduction

TODO

## Exercise Tasks
1. In the `HelloDic` class, write an algorithm deciding based on the "leading" Task's input parameter `tutorial-input`, whether the `highmedorg_helloCos` process should be started.
2. Add a boolean variable to the process instance execution variables storing the decision.
3. Add an exclusive gateway to the `highmedorg_helloDic` process model and two outgoing sequence flows - the first starting process `highmedorg_helloDic`, the second stopping process `highmedorg_helloDic` without starting process `highmedorg_helloCos`.
4. Add a condition expressions to each outgoing sequence flows based on the previously stored execution variable.

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:

```
mvn clean install -Pexercise-4
```

Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `highmedorg_helloDic` and `highmedorg_helloCos` processes can be executed successfully, we need to deploy
them into DSF instances and execute the `highmedorg_helloDic` process. The maven `install` build is configured to create
a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker test setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at
   location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in another console at
   location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmed_helloDic` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a console at
   location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server
   at https://cos/fhir  
   The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate
   yourself to the server you can use the client certificate located
   at `.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (
   Password: password)

4. Start the DSF BPE server for the `Test_COS` organization in another console at
   location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmed_helloCos` process. The DSF BPE server should
   print a message that the process was deployed. The DSF FHIR server should now have a new ActivityDefinition resource.
   Go to https://cos/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying
   the process. The returned FHIR Bundle should contain a two ActivityDefinition resources. Also, go
   to https://cos/fhir/StructureDefinition?url=http://highmed.org/fhir/StructureDefinition/task-hello-cos to check if
   the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

5. Start the `highmed_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to
   the DSF FHIR server of the `Test_DIC` organization:
   Executing the `main` method of the `org.highmed.dsf.process.tutorial.TutorialExampleStarter` class to create
   the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `highmed_helloDic` process.

   Verify that the `highmedorg_helloDic` process was executed successfully by the `Test_DIC` DSF BPE server and possibly
   the `highmedorg_helloCos` process by the `Test_COS` DSF BPE server, depending on whether the correct code word was
   sent as input parameter to start the `highmedorg_helloDic` process.