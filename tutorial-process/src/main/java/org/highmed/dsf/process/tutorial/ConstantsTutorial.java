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

	String CODESYSTEM_TUTORIAL = "http://highmed.org/fhir/CodeSystem/tutorial";
	String CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT = "tutorial-input";
}
