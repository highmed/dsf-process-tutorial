# Exercise 2 - Input Parameter
In order to dynamically control processes that are packaged as a process plugins, we will take a look in this exercise at two possibilities on how to pass input parameters to a process.

## Introduction
TODO

## Exercise Tasks
1. TODO

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:
```
mvn clean install -Pexercise-2
```
Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `highmedorg_helloDic` process can be executed successfully, we need to deploy it into a DSF instance and execute the process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker test setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://dic/fhir  
   The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate yourself to the server you can use the client certificate located at `.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (Password: password)

2. Start the DSF BPE server for the `Test_DIC` organization in another console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmed_helloDic` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new ActivityDefinition resource. Go to https://dic/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR Bundle should contain a single ActivityDefinition. Also, go to https://dic/fhir/StructureDefinition?url=http://highmed.org/fhir/StructureDefinition/task-hello-dic to check if the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

3. Start the `highmed_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server:

   The [Task](http://hl7.org/fhir/R4/task.html) resource is used to tell the DSF BPE server via the DSF FHIR server that a specific organization wants to start (or continue) one process instance at a specified organization. The needed [Task](http://hl7.org/fhir/R4/task.html) resource can be generated and posted to the DSF FHIR server by executing the `main` method of the `org.highmed.dsf.process.tutorial.TutorialExampleStarter` class. For TutorialExampleStarter to work the location of the client certificate and its password need to be specified:
    * Either specify the location and password via program arguments: 1. location of the client certificate (`.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12`), 2. password for the client certificate (`password`)
    * Or set the environment variables `DSF_CLIENT_CERTIFICATE_PATH` and `DSF_CLIENT_CERTIFICATE_PASSWORD` with the appropriate values.

   Verify that the FHIR [Task](http://hl7.org/fhir/R4/task.html) resource could be created at the DSF FHIR server. The TutorialExampleStarter class should print a message `HTTP 201: Created` showing that the [Task](http://hl7.org/fhir/R4/task.html) resource was created.

   Verify that the `highmedorg_helloDic` process was executed by the DSF BPE server. The BPE server should:
   * Print a message showing that the process was started.
   * If logging is enabled: print the log message **and the value of the input parameter** you added to the `HelloDic` implementation.
   * Print a message showing that the process finished.