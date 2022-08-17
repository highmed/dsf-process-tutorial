package org.highmed.dsf.process.tutorial.exercise_5.message;

import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_INSTANTIATES_URI;
import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_LEADING_TASK;
import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_MESSAGE_NAME;
import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_PROFILE;
import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_TARGET;
import static org.highmed.dsf.bpe.ConstantsBase.NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI_AND_LATEST_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import java.util.UUID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.organization.OrganizationProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.highmed.dsf.fhir.variables.Target;
import org.highmed.dsf.process.tutorial.message.HelloCosMessage;
import org.highmed.fhir.client.FhirWebserviceClient;
import org.highmed.fhir.client.PreferReturnMinimalWithRetry;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Task.ParameterComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ca.uhn.fhir.context.FhirContext;

@RunWith(MockitoJUnitRunner.class)
public class HelloCosMessageTest
{
	@Mock
	private FhirWebserviceClientProvider clientProvider;

	@Mock
	private FhirWebserviceClient client;

	@Mock
	private PreferReturnMinimalWithRetry clientWithMinimalReturn;

	@Mock
	private TaskHelper taskHelper;

	@Mock
	private ReadAccessHelper readAccessHelper;

	@Mock
	private OrganizationProvider organizationProvider;

	@Mock
	private DelegateExecution execution;

	@Test
	public void testGetAdditionalInputParameters() throws Exception
	{
		HelloCosMessage messageDelegate = new HelloCosMessage(clientProvider, taskHelper, readAccessHelper,
				organizationProvider, FhirContext.forR4());

		Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_TARGET))
				.thenReturn(Target.createUniDirectionalTarget("Test_COS", "Test_COS_Endpoint", "https://cos/fhir"));

		Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_INSTANTIATES_URI))
				.thenReturn(PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI_AND_LATEST_VERSION);
		Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_MESSAGE_NAME))
				.thenReturn(PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME);
		Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_PROFILE))
				.thenReturn(PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION);
		Mockito.when(execution.getBusinessKey()).thenReturn(UUID.randomUUID().toString());

		Mockito.when(clientProvider.getWebserviceClient(anyString())).thenReturn(client);
		Mockito.when(client.getBaseUrl()).thenReturn("https://cor/fhir");
		Mockito.when(client.withMinimalReturn()).thenReturn(clientWithMinimalReturn);

		Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK)).thenReturn(getTask());

		Mockito.when(taskHelper.getFirstInputParameterStringValue(any(),
				eq("http://highmed.org/fhir/CodeSystem/tutorial"), eq("tutorial-input")))
				.thenReturn(Optional.of("Test"));

		Mockito.when(taskHelper.createInput(eq("http://highmed.org/fhir/CodeSystem/tutorial"), eq("tutorial-input"),
				eq("Test")))
				.thenReturn(new ParameterComponent(
						new CodeableConcept(
								new Coding("http://highmed.org/fhir/CodeSystem/tutorial", "tutorial-input", null)),
						new StringType("Test")));

		messageDelegate.execute(execution);

		Mockito.verify(execution).getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK);
		ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
		Mockito.verify(clientWithMinimalReturn).create(captor.capture());

		Task sendTask = captor.getValue();
		assertNotNull(sendTask);
		assertEquals(3, sendTask.getInput().size());

		ParameterComponent tutorialInput = sendTask.getInput().get(2);
		assertEquals(1,
				tutorialInput.getType().getCoding().stream()
						.filter(c -> "http://highmed.org/fhir/CodeSystem/tutorial".equals(c.getSystem()))
						.filter(c -> "tutorial-input".equals(c.getCode())).count());
		assertTrue(tutorialInput.getValue() instanceof StringType);
		assertEquals("Test", ((StringType) tutorialInput.getValue()).getValue());
	}

	private Task getTask()
	{
		Task task = new Task();
		task.getRestriction().addRecipient().getIdentifier().setSystem(NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER)
				.setValue("MeDIC");
		task.addInput().setValue(new StringType("Test")).getType().addCoding()
				.setSystem("http://highmed.org/fhir/CodeSystem/tutorial").setCode("tutorial-input");

		return task;
	}
}