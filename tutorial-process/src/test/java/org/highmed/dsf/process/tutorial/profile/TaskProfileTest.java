package org.highmed.dsf.process.tutorial.profile;

import static org.highmed.dsf.bpe.ConstantsBase.CODESYSTEM_HIGHMED_BPMN;
import static org.highmed.dsf.bpe.ConstantsBase.CODESYSTEM_HIGHMED_BPMN_VALUE_MESSAGE_NAME;
import static org.highmed.dsf.bpe.ConstantsBase.NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_WORLD;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_WORLD_MESSAGE_NAME;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_WORLD_PROCESS_URI_AND_LATEST_VERSION;
import static org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition.RELEASE_DATE;
import static org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition.VERSION;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;

import org.highmed.dsf.fhir.validation.ResourceValidator;
import org.highmed.dsf.fhir.validation.ResourceValidatorImpl;
import org.highmed.dsf.fhir.validation.ValidationSupportRule;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Task.TaskIntent;
import org.hl7.fhir.r4.model.Task.TaskStatus;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;

public class TaskProfileTest
{
	private static final Logger logger = LoggerFactory.getLogger(TaskProfileTest.class);

	@ClassRule
	public static final ValidationSupportRule validationRule = new ValidationSupportRule(VERSION, RELEASE_DATE,
			Arrays.asList("highmed-task-base-0.5.0.xml", "task-hello-world.xml"),
			Arrays.asList("highmed-read-access-tag-0.5.0.xml", "highmed-bpmn-message-0.5.0.xml"),
			Arrays.asList("highmed-read-access-tag-0.5.0.xml", "highmed-bpmn-message-0.5.0.xml"));

	private ResourceValidator resourceValidator = new ResourceValidatorImpl(validationRule.getFhirContext(),
			validationRule.getValidationSupport());

	@Test
	public void testTaskHelloWorldValid()
	{
		Task task = createValidTaskHelloWorld();

		ValidationResult result = resourceValidator.validate(task);
		ValidationSupportRule.logValidationMessages(logger, result);

		assertEquals(0, result.getMessages().stream().filter(m -> ResultSeverityEnum.ERROR.equals(m.getSeverity())
				|| ResultSeverityEnum.FATAL.equals(m.getSeverity())).count());
	}

	private Task createValidTaskHelloWorld()
	{
		Task task = new Task();
		task.getMeta().addProfile(PROFILE_TUTORIAL_TASK_HELLO_WORLD);
		task.setInstantiatesUri(PROFILE_TUTORIAL_TASK_HELLO_WORLD_PROCESS_URI_AND_LATEST_VERSION);
		task.setStatus(TaskStatus.REQUESTED);
		task.setIntent(TaskIntent.ORDER);
		task.setAuthoredOn(new Date());
		task.getRequester().setType(ResourceType.Organization.name()).getIdentifier()
				.setSystem(NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER).setValue("MeDIC");
		task.getRestriction().addRecipient().setType(ResourceType.Organization.name()).getIdentifier()
				.setSystem(NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER).setValue("MeDIC");

		task.addInput().setValue(new StringType(PROFILE_TUTORIAL_TASK_HELLO_WORLD_MESSAGE_NAME)).getType().addCoding()
				.setSystem(CODESYSTEM_HIGHMED_BPMN).setCode(CODESYSTEM_HIGHMED_BPMN_VALUE_MESSAGE_NAME);

		return task;
	}
}
