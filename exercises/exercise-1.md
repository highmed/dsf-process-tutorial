# Exercise 1 - Simple Process
The first exercise focuses on setting up the testing environment used in this tutorial and shows how to implement and execute a simple BPMN process.

## Introduction
The tutorial project consists of three parts: A `test-data-generator` project used to generate X.509 certificates and FHIR resources during the build of the project. The certificates and FHIR resources are needed to start DSF instances simulating installations at three different organizations used for this tutorial. The DSF instances are configured using a `docker-compose.yml` file in the `test-setup` folder. The docker-compose test setup uses a single PostgreSQL database server and a single nginx reverse proxy as well as three separate DSF FHIR server- and 3 separate DSF BPE server instances. The `tutorial-process` project contains all resource (FHIR resources, BPMN process models and Java code for the actual DSF process plugin.

The `tutorial-process` project contains Java code at `src/main/java`, FHIR resources and BPMN process models at `src/main/resources` as well as prepared JUnit tests to verify your solution at `src/test/java`.

The most imported Java class used to specify the process plugin for the DSF BPE server is a class that implements the `org.highmed.dsf.bpe.ProcessPluginDefinition` interface from the `dsf-bpe-process-base` module. The DSF BPE server searches for classes implementing this interface using the [ServiceLoader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html) mechanism. For this tutorial the `TutorialProcessPluginDefinition` class implements this interface and appropriately the `TutorialProcessPluginDefinition` class is specified in the `src/main/resources/META-INF/services/org.highmed.dsf.bpe.ProcessPluginDefinition` file.

The `TutorialProcessPluginDefinition` class is used to specify name and version of the process plugin, what BPMN process are to be deployed and what FHIR resources and required by the BPMN process as well as specifying Java service classes implementing process tasks and message events via [Spring-Framework beans defined in a configuration class](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-java-basic-concepts): `TutorialConfig`.

## Exercise Tasks
1. Add a log message to the `HelloDic#doExecute` method that contains the recipient organization identifier from the "leading" Task.
1. Register the `HelloDic` class as a singleton bean in the `TutorialConfig` class.
1. Set the `HelloDic` class as the service implementation of the appropriate service task within the `hello-dic.bpmn` process model.
1. Modify the ActivityDefinition for the `highmedorg_helloDic` process to only allow local clients to instantiate the process via a `helloDic` message.

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:
```
mvn clean install -Pexercise-1
```
Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `highmedorg_helloDic` process can be execute successfully, we need to deploy it into a DSF instance and execute the process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker test setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/test-setup`:
	```
	docker-compose up dic-fhir
	```
	Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://dic/fhir  
	The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate yourself to the server you can use the client certificate located at `.../test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (Password: password)

2. Start the DSF BPE server for the `Test_DIC` organization in another console at location `.../dsf-process-tutorial/test-setup`:
	```
	docker-compose up dic-bpe
	```
	Verify the DSF BPE server started successfully and deployed the `highmed_helloDic` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new ActivityDefinition resource. Go to https://dic/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR Bundle should contain a single ActivityDefinition. Also, go to https://dic/fhir/StructureDefinition?url=http://highmed.org/fhir/StructureDefinition/task-hello-dic to check if the expected Task profile was created.

3. Start the `highmed_helloDic` process by posting a specific FHIR Task resource to the DSF FHIR server:

    The Task resource is used to tell the DSF BPE server via the DSF FHIR server that a specific organization wants to start (or continue) one process instance at a specified organization. The needed Task resource can be generated and posted to the DSF FHIR server by executing the `main` method of the `org.highmed.dsf.process.tutorial.TutorialExampleStarter` class. For TutorialExampleStarter to work the location of the client certificate and its password need to be specified:
	* Either specify the location and password via program arguments: 1. location of the client certificate (`.../test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12`), 2. password for the client certificate (`password`)
	* Or set the environment variables `DSF_CLIENT_CERTIFICATE_PATH` and `DSF_CLIENT_CERTIFICATE_PASSWORD` with the appropriate values.
	
	Verify that the FHIR Task resource could be created at the DSF FHIR server. The TutorialExampleStarter class should print a message `HTTP 201: Created` showing that the Task resource was created.
	
	Verify that the `highmedorg_helloDic` process was executed by the DSF BPE server. The BPE server should print a message showing that the process was started, print the log message you added to the `HelloDic` class and end with a message showing that the process finished.