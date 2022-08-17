package org.highmed.dsf.process.tutorial;

import static org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition.VERSION;

public interface ConstantsTutorial
{
	String PROCESS_NAME_HELLO_DIC = "helloDic";
	String PROCESS_NAME_FULL_HELLO_DIC = "highmedorg_" + PROCESS_NAME_HELLO_DIC;

	String PROFILE_TUTORIAL_TASK_HELLO_DIC = "http://highmed.org/fhir/StructureDefinition/task-hello-dic";
	String PROFILE_TUTORIAL_TASK_HELLO_DIC_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_DIC + "|" + VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_DIC_PROCESS_URI = "http://highmed.org/bpe/Process/" + PROCESS_NAME_HELLO_DIC
			+ "/";
	String PROFILE_TUTORIAL_TASK_HELLO_DIC_PROCESS_URI_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_DIC_PROCESS_URI
			+ VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_DIC_MESSAGE_NAME = "helloDic";

	String PROFILE_TUTORIAL_TASK_GOODBYE_DIC = "http://highmed.org/fhir/StructureDefinition/task-goodbye-dic";
	String PROFILE_TUTORIAL_TASK_GOODBYE_DIC_MESSAGE_NAME = "goodbyeDic";

	// The HELLO_COS constants are only needed for exercise 3 and above
	String PROCESS_NAME_HELLO_COS = "helloCos";
	String PROCESS_NAME_FULL_HELLO_COS = "highmedorg_" + PROCESS_NAME_HELLO_COS;

	String PROFILE_TUTORIAL_TASK_HELLO_COS = "http://highmed.org/fhir/StructureDefinition/task-hello-cos";
	String PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_COS + "|" + VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI = "http://highmed.org/bpe/Process/" + PROCESS_NAME_HELLO_COS
			+ "/";
	String PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI
			+ VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME = "helloCos";

	// The HELLO_HRP constants are only needed for exercise 5 and above
	String PROCESS_NAME_HELLO_HRP = "helloHrp";
	String PROCESS_NAME_FULL_HELLO_HRP = "highmedorg_" + PROCESS_NAME_HELLO_HRP;

	String PROFILE_TUTORIAL_TASK_HELLO_HRP = "http://highmed.org/fhir/StructureDefinition/task-hello-hrp";
	String PROFILE_TUTORIAL_TASK_HELLO_HRP_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_HRP + "|" + VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_HRP_PROCESS_URI = "http://highmed.org/bpe/Process/" + PROCESS_NAME_HELLO_HRP
			+ "/";
	String PROFILE_TUTORIAL_TASK_HELLO_HRP_PROCESS_URI_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_HRP_PROCESS_URI
			+ VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_HRP_MESSAGE_NAME = "helloHrp";

	String CODESYSTEM_TUTORIAL = "http://highmed.org/fhir/CodeSystem/tutorial";
	String CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT = "tutorial-input";
}
