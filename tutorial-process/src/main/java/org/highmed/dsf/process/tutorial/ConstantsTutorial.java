package org.highmed.dsf.process.tutorial;

import static org.highmed.dsf.bpe.ConstantsBase.PROCESS_HIGHMED_URI_BASE;
import static org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition.VERSION;

public interface ConstantsTutorial
{
	String PROCESS_NAME_HELLO_WORLD = "helloWorld";
	String PROCESS_NAME_FULL_HELLO_WORLD = "highmedorg_" + PROCESS_NAME_HELLO_WORLD;

	String PROFILE_TUTORIAL_TASK_HELLO_WORLD = "http://highmed.org/fhir/StructureDefinition/task-hello-world";
	String PROFILE_TUTORIAL_TASK_HELLO_WORLD_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_WORLD + "|" + VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_WORLD_PROCESS_URI = PROCESS_HIGHMED_URI_BASE + PROCESS_NAME_HELLO_WORLD + "/";
	String PROFILE_TUTORIAL_TASK_HELLO_WORLD_PROCESS_URI_AND_LATEST_VERSION = PROFILE_TUTORIAL_TASK_HELLO_WORLD_PROCESS_URI
			+ VERSION;
	String PROFILE_TUTORIAL_TASK_HELLO_WORLD_MESSAGE_NAME = "helloWorldMessage";
}
