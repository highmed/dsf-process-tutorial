# Exercise 2 - Input Parameter
In order to configure processes that are packaged as process plugins, we will take a look at two possibilities on how to pass parameters to a process. The goal of this exercise is to enhance the `highmedorg_helloDic` process by trying them both.

## Introduction
DSF process plugins can be configured with input parameters using two different approaches: 

* Static configuration using environment variables during the deployment of a process plugin 
* Dynamic configuration by sending values as part of the [Task](http://hl7.org/fhir/R4/task.html) resource to start or continue a process instance

### Environment Variables
Environment variables are the same for all running process instances and allow static configuration of processes. They can be defined by adding a member variable having the [Spring-Framework @Value](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-value-annotations) annotation to the configuration class `TutorialConfig`. The value of the annotation uses the `${..}` notation and follows the form `${some.property:defaultValue}`, where each dot in the property name corresponds to an underscore in the environment variable and environment variables are always written uppercase. The property `some.property` therefore corresponds to the environment variable `SOME_PROPERTY`.

To create an automated documentation of environment variables during the Maven build process, the [@ProcessDocumentation](https://github.com/highmed/highmed-dsf/blob/main/dsf-tools/dsf-tools-documentation-generator/src/main/java/org/highmed/dsf/tools/generator/ProcessDocumentation.java) annotation can be used. The `pom.xml` of the `tutorial-process` submodule calls the [DocumentGenerator](https://github.com/highmed/highmed-dsf/blob/main/dsf-tools/dsf-tools-documentation-generator/src/main/java/org/highmed/dsf/tools/generator/DocumentationGenerator.java) class during the `prepare-package` phase of the build process, which searches for all [@ProcessDocumentation](https://github.com/highmed/highmed-dsf/blob/main/dsf-tools/dsf-tools-documentation-generator/src/main/java/org/highmed/dsf/tools/generator/ProcessDocumentation.java) annotations and generates a Markdown documentation based on the annotation's values.

### Task Input Parameters
Providing input parameters to a specific process instance allows for dynamic configuration of process instances. It can be done by sending additional values as part of the [Task](http://hl7.org/fhir/R4/task.html) resource that starts or continues a process instance. It should be noted that a FHIR profile must be created for each [Task](http://hl7.org/fhir/R4/task.html) resource, i.e. for each message event in a process model, which inherits from the [DSF Task Base Profile](https://github.com/highmed/highmed-dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/highmed-task-base-0.5.0.xml). This base profile defines three default input parameters:

* [`message-name`](https://github.com/highmed/highmed-dsf/blob/f372b757b22d59b3594a220f7f380c60aa6f00b8/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/highmed-task-base-0.5.0.xml#L106-L145) (**mandatory 1..1**): the name of the BPMN message event, same as in the BPMN model
* [`business-key`](https://github.com/highmed/highmed-dsf/blob/f372b757b22d59b3594a220f7f380c60aa6f00b8/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/highmed-task-base-0.5.0.xml#L146-L184) (optional 0..1): used to identify process instances
* [`correlation-key`](https://github.com/highmed/highmed-dsf/blob/f372b757b22d59b3594a220f7f380c60aa6f00b8/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/highmed-task-base-0.5.0.xml#L185-L223) (optional 0..1): used to identify multi-instance process instances used for messaging multiple targets

Since input parameters  of [Task](http://hl7.org/fhir/R4/task.html) resources are identified by predefined codes, they are defined via FHIR [CodeSystem](http://hl7.org/fhir/R4/codesystem.html) and [ValueSet](http://hl7.org/fhir/R4/valueset.html) resources. The [BPMN-Message CodeSystem](https://github.com/highmed/highmed-dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/highmed-bpmn-message-0.5.0.xml) and the [BPMN-Message ValueSet](
https://github.com/highmed/highmed-dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/ValueSet/highmed-bpmn-message-0.5.0.xml) are used in the [DSF Task Base Profile](https://github.com/highmed/highmed-dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/highmed-task-base-0.5.0.xml) to define the three default input parameters of [Task](http://hl7.org/fhir/R4/task.html) resources.

### Version and Release-Date Placeholders
To avoid the need to enter the version and release date for each [Task](http://hl7.org/fhir/R4/task.html) profile, [CodeSystem](http://hl7.org/fhir/R4/codesystem.html) and [ValueSet](http://hl7.org/fhir/R4/valueset.html) release, the placeholders `#{version}` and `#{date}` can be used. They are replaces with the values returned by the methods `ProcessPluginDefinition#getVersion()` and `ProcessPluginDefinition#getReleaseDate()` respectivally during deployment of a process plugin.

### Read Access Tag
While writing FHIR resources on the DSF FHIR server is only allowed by the own organization, rules have to be defined for reading FHIR resources by external organizations (except [Task](http://hl7.org/fhir/R4/task.html). The `Resource.meta.tag` field is used for this purpose. To allow read access for all, the following `read-access-tag` value can be written into this field:

```xml
<meta>
   <tag>
      <system value="http://highmed.org/fhir/CodeSystem/read-access-tag" />
      <code value="ALL" />
   </tag>
</meta>
```

The read access rules for [Task](http://hl7.org/fhir/R4/task.html) resources are defined through the fields `Task.requester` and `Task.restriction.recipient`. Therefore, no `read-access-tag` is needed.

It is also possible to restrict the reading of FHIR resources to a role in a consortium or to a specific organization. We will discuss this in exercise 5.

## Exercise Tasks
1. Add an environment variable to enable/disable logging to the `TutorialConfig` class using the above explained
   annotations and add a default value `false`.
2. Use the new environment variable in the `HelloDic` class to decide whether the log message from exercise 1 should be
   printed.
3. Adapt `test-setup/docker-compose.yml` by adding the new environment variable to the service `dic-bpe` and set the
   value to `true`.
4. Create a new CodeSystem with url `http://highmed.org/fhir/CodeSystem/tutorial` having a concept with
   code `tutorial-input`.
5. Create a new ValueSet with url `http://highmed.org/fhir/ValueSet/tutorial` including all concepts from the
   CodeSystem.
6. Add the new CodeSystem and ValueSet resources to the `highmedorg_helloDic` process in
   the `TutorialProcessPluginDefinition` class.
7. Add a new input parameter of type `string` to the `task-hello-dic.xml` [Task](http://hl7.org/fhir/R4/task.html)
   profile using the concept of the new CodeSystem as a fixed coding.
8. Read the new input parameter in the `HelloDic` class from the "leading" [Task](http://hl7.org/fhir/R4/task.html) and
   add the value to log message from exercise 1
9. Adapt the starter class `TutorialExampleStarter` by adding the new input parameter with an arbitrary string.

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:

```
mvn clean install -Pexercise-2
```

Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `highmedorg_helloDic` process can be executed successfully, we need to deploy it into a DSF instance and
execute the process. The maven `install` build is configured to create a process jar file with all necessary resources
and copy the jar to the appropriate locations of the docker test setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at
   location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server
   at https://dic/fhir  
   The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate
   yourself to the server you can use the client certificate located
   at `.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (
   Password: password)

2. Start the DSF BPE server for the `Test_DIC` organization in another console at
   location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `highmedorg_helloDic` process. The DSF BPE server should
   print a message that the process was deployed. The DSF FHIR server should now have a new ActivityDefinition resource.
   Go to https://dic/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying
   the process. The returned FHIR Bundle should contain a single ActivityDefinition. Also, go
   to https://dic/fhir/StructureDefinition?url=http://highmed.org/fhir/StructureDefinition/task-hello-dic to check if
   the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

3. Start the `highmedorg_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Executing the `main` method of the `org.highmed.dsf.process.tutorial.TutorialExampleStarter` class to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `highmedorg_helloDic` process.

   Verify that the `highmedorg_helloDic` process was executed by the DSF BPE server. The BPE server should:
    * Print a message showing that the process was started.
    * If logging is enabled - print the log message and the value of the input parameter you added to the `HelloDic`
      implementation.
    * Print a message showing that the process finished.