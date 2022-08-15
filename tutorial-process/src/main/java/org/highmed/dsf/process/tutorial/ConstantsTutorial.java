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
}
