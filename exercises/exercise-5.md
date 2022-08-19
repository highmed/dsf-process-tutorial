# Exercise 5 - Event Gateway and Intermediate Events
In the final exercise we look at message flow between three organizations as well as continuing a process if no return message arrives.

## Introduction
### Managing Multiple- and Missing Messages
If an existing and started process instance is waiting for a message from another organization, the corresponding FHIR [Task](http://hl7.org/fhir/R4/task.html) may never arrive. Either because the other organization decides to never send the "message" or because some technical problem prohibits the [Task](http://hl7.org/fhir/R4/task.html) resource from being posted to the DSF FHIR server. This would result in stale process instances that never finish.

In order to solve this problem we can add a [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/) to the process waiting for a response and than either handle a [Task](http://hl7.org/fhir/R4/task.html) resource with the response and finish the process in a success state or fire of a [Intermediate Timer Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event) after a defined wait period and finish the process in an error state. The following BPMN collaboration diagram shows how the process at the first organization would look like if two different message or no message could be received:

![BPMN collaboration diagram with Event Based Gateway](figures/exercise5_event_based_gateway.svg)

### Matching Process Instances With Business Keys
In the example above the first organization is sending a "message" to the second and waiting for a reply. In order to correlate the return message with the waiting process instance, a unique identifier needs to be exchanged between both process instances. Within the DSF this is implemented using the process instance _business-key_ and a corresponding [Task.input](http://hl7.org/fhir/R4/task.html) parameter. For **1:1** communication relationships this is handled by the DSF BPE servers automatically, but the corresponding [Task](http://hl7.org/fhir/R4/task.html) profiles need to define the _business-key_ input parameter as mandatory.

If multiple message are send in a **1:n** relationship with a **n:1** return an additional _correlation-key_ needs to be configured in order to correlate every bidirectional communication between two DSF instances.

## Exercise Tasks
1. Modify the `HelloCosMessage` and use the value from Task.input parameter of the `helloDic` [Task](http://hl7.org/fhir/R4/task.html) to send it to the `highmedorg_helloCos` process via a [Task.input](http://hl7.org/fhir/R4/task.html) parameter in the `helloCos` Task. Override the `getAdditionalInputParameters` to configure a [Task.input](http://hl7.org/fhir/R4/task.html) parameter to be send.
1. Modify the `highmedorg_helloCos` process to use a [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) to trigger the process in file `hello-hrp.bpmn`. Figure out the values for the `instantiatesUri`, `profile` and `messageName` input parameters of the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) based on the [AcitvityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) in file `hello-hrp.xml`.
1. Modify the `highmedorg_helloDic` process:
	* Change the[Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) to a [Intermediate Message Throw Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-throwing-event)
	* Add a [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/) after the throw event
	* Configure two cases for the [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/):
	    1. A [Intermediate Message Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-catching-event) to catch the `goodbyDic` message from the `highmedorg_helloHrp` process.
	    1. A [Intermediate Timer Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event) to end the process if no message is send by the `highmedorg_helloHrp` process after two minutes.
	    Make sure both cases finish with a process [End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/none-events/).
1. Modify the process in file `hello-hrp.bpmn` and set the _process definition key_ and _version_. Figure out the appropriate values based on the [AcitvityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) in file `hello-hrp.xml`.
1. Add the process in file `hello-hrp.bpmn` to the `TutorialProcessPluginDefinition` and configure the FHIR resources needed for the three processes.
1. Add the `HelloCos`, `HelloHrpMessage `, `HelloHrp` and `GoodbyeDicMessage` classes as spring beans.

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
   Verify the DSF BPE server started successfully and deployed the `highmedorg_helloDic` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully.

4. Start the DSF BPE server for the `Test_COS` organization in another console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmedorg_helloDic` process.


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
   Verify the DSF BPE server started successfully and deployed the `highmedorg_helloHrp` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new ActivityDefinition resource. Go to https://hrp/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR Bundle should contain a three ActivityDefinition resources. Also, go to https://hrp/fhir/StructureDefinition?url=http://highmed.org/fhir/StructureDefinition/task-hello-hrp to check if the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

7. Start the `highmedorg_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Executing the `main` method of the `org.highmed.dsf.process.tutorial.TutorialExampleStarter` class to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `highmedorg_helloDic` process.

   Verify that the FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server and the `highmedorg_helloDic` process was executed by the DSF BPE server of the `Test_DIC` organization. The DSF BPE server of the `Test_DIC` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `highmedorg_helloCos` process was send to the `Test_COS` organization.  
   Verify that a FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server of the `Test_COS` organization and the `highmedorg_helloCos` process was executed by the DSF BPE server of the `Test_COS` organization. The DSF BPE server of the `Test_COS` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `highmedorg_helloHrp` process was send to the `Test_HRP` organization.  
   
   Based on the value of the Task.input parameter you send, the `highmedorg_helloHrp` process will either send a `goodbyDic` message to the `Test_DIC` organization or finish without sending a message.
   
   To trigger the `goodbyDic` message, use `send-response` as the `http://highmed.org/fhir/CodeSystem/tutorial#tutorial-input` input parameter.
   
   Verify that the `highmedorg_helloDic` process either finishes with the arrival of the `goodbyDic` message or after waiting for two minutes.