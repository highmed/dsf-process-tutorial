# Exercise 5 - Event Gateway and Intermediate Events
In the final exercise we look at message flow between three organizations as well as continuing a process if no return message arrives.

## Introduction
TODO Event Based Gateway, Business Key based correlation, relation ship between process key / version and instantiatesUri (see exercise 3).

## Exercise Tasks
1. Modify the `HelloCosMessage` to send the Task.input parameter from the `helloDic` Task to be send to the `highmed_helloCos` process via a Task.input parameter in the `helloCos` Task. Override the `getAdditionalInputParameters` to configure an Task.input parameter to be send.
1. Modify the `highmed_helloCos` process to use a Message End Event to trigger the process in file `hello-hrp.bpmn` figure out the values for the `instantiatesUri`, `profile` and `messageName` input parameters based on the ActivityDefinition in file `hello-hrp.xml`.
1. Modify the `highmed_helloDic` process:
	* Change the Message End Event to a Intermediate Message Throw Event
	* Add a Event Based Gateway after the throw event
	* Configure two cases for the event based gateway:
	    1. A Intermediate Message Catch Event to catch the `goodbyDic` message from the `highmed_helloHrp` process.
	    1. A Intermediate Timer Event to end the process if no message is send by the `highmed_helloHrp` process after two minutes.
	    Make sure both cases end with a process End Event.
1. Modify the process in file `hello-hrp.bpmn` and set the process definition key and version. Figure out the appropriate values based on the AcitvityDefinition in file `hello-hrp.xml`.
1. Add the process in file `hello-hrp.bpmn` to the `TutorialProcessPluginDefinition` and configure the FHIR resources needed for the three processes.
1. Configure `HelloCos`, `HelloHrpMessage `, `HelloHrp` and `GoodbyeDicMessage` as spring bean.

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:
```
mvn clean install -Pexercise-5
```
Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `highmedorg_helloDic` and `highmedorg_helloCos` processes can be executed successfully, we need to deploy them into DSF instances and execute the `highmedorg_helloDic` process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker test setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in another console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmed_helloDic` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully.

4. Start the DSF BPE server for the `Test_COS` organization in another console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmed_helloDic` process.


5. Start the DSF FHIR server for the `Test_HRP` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up hrp-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://hrp/fhir  
   The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate yourself to the server you can use the client certificate located at `.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (Password: password)

6. Start the DSF BPE server for the `Test_COS` organization in another console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up hrp-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmed_helloHrp` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new ActivityDefinition resource. Go to https://hrp/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR Bundle should contain a three ActivityDefinition resources. Also, go to https://hrp/fhir/StructureDefinition?url=http://highmed.org/fhir/StructureDefinition/task-hello-hrp to check if the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

7. Start the `highmed_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Executing the `main` method of the `org.highmed.dsf.process.tutorial.TutorialExampleStarter` class to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `highmed_helloDic` process.

   Verify that the FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server and the `highmedorg_helloDic` process was executed by the DSF BPE server of the `Test_DIC` organization. The DSF BPE server of the `Test_DIC` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `highmed_helloCos` process was send to the `Test_COS` organization.  
   Verify that a FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server of the `Test_COS` organization and the `highmedorg_helloCos` process was executed by the DSF BPE server of the `Test_COS` organization. The DSF BPE server of the `Test_COS` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `highmed_helloHrp` process was send to the `Test_HRP` organization.  
   
   Based on the value of the Task.input parameter you send, the `highmed_helloHrp` process will either send a `goodbyDic` message to the `Test_DIC` organization or finish without sending a message.
   
   To trigger the `goodbyDic` message, use `send-response` as the `http://highmed.org/fhir/CodeSystem/tutorial#tutorial-input` input parameter.
   
   Verify that the `highmedorg_helloDic` process either finishes with the arrival of the `goodbyDic` message or after waiting for two minutes.