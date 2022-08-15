package org.highmed.dsf.process.tutorial.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld extends AbstractServiceDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

	public HelloWorld(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
			ReadAccessHelper readAccessHelper)
	{
		super(clientProvider, taskHelper, readAccessHelper);
	}

	@Override
	protected void doExecute(DelegateExecution execution)
	{
		logger.info("Hello World from organization '{}'", getLeadingTaskFromExecutionVariables().getRestriction()
				.getRecipientFirstRep().getIdentifier().getValue());
	}
}
