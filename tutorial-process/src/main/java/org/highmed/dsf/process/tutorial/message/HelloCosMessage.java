package org.highmed.dsf.process.tutorial.message;

import static org.highmed.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT;

import java.util.Optional;
import java.util.stream.Stream;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.organization.OrganizationProvider;
import org.highmed.dsf.fhir.task.AbstractTaskMessageSend;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.hl7.fhir.r4.model.Task;

import ca.uhn.fhir.context.FhirContext;

// Only needed for exercise 3 and above
public class HelloCosMessage extends AbstractTaskMessageSend
{
	public HelloCosMessage(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
			ReadAccessHelper readAccessHelper, OrganizationProvider organizationProvider, FhirContext fhirContext)
	{
		super(clientProvider, taskHelper, readAccessHelper, organizationProvider, fhirContext);
	}

	@Override
	protected Stream<Task.ParameterComponent> getAdditionalInputParameters(DelegateExecution execution)
	{
		Optional<String> tutorialInputParameter = getTaskHelper().getFirstInputParameterStringValue(
				getLeadingTaskFromExecutionVariables(), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT);

		return tutorialInputParameter
				.map(s -> getTaskHelper().createInput(CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT, s))
				.stream();
	}
}
