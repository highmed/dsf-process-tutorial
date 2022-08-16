package org.highmed.dsf.process.tutorial.service;

import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_TARGET;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT;

import java.util.Optional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.highmed.dsf.fhir.variables.Target;
import org.highmed.dsf.fhir.variables.TargetValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloDic extends AbstractServiceDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(HelloDic.class);

	private final boolean loggingEnabled;

	public HelloDic(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
			ReadAccessHelper readAccessHelper, boolean loggingEnabled)
	{
		super(clientProvider, taskHelper, readAccessHelper);

		this.loggingEnabled = loggingEnabled;
	}

	@Override
	protected void doExecute(DelegateExecution execution)
	{
		if (loggingEnabled)
		{
			Optional<String> tutorialInputParameter = getTaskHelper().getFirstInputParameterStringValue(
					getLeadingTaskFromExecutionVariables(), CODESYSTEM_TUTORIAL,
					CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT);

			logger.info(
					"Hello Dic from organization '{}' with message '{}'", getLeadingTaskFromExecutionVariables()
							.getRestriction().getRecipientFirstRep().getIdentifier().getValue(),
					tutorialInputParameter.orElse("<no message>"));
		}

		Target target = Target.createUniDirectionalTarget("Test_COS", "Test_COS_Endpoint", "https://cos/fhir");
		execution.setVariable(BPMN_EXECUTION_VARIABLE_TARGET, TargetValues.create(target));
	}
}
