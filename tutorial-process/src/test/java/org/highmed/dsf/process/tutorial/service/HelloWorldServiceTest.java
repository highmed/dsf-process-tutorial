package org.highmed.dsf.process.tutorial.service;

import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_LEADING_TASK;
import static org.highmed.dsf.bpe.ConstantsBase.NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER;
import static org.junit.Assert.fail;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class HelloWorldServiceTest
{
	private static final Logger logger = LoggerFactory.getLogger(HelloWorldServiceTest.class);

	@Mock
	private DelegateExecution execution;

	@InjectMocks
	private HelloWorld service;

	@Test
	public void testValid()
	{
		try
		{
			Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK)).thenReturn(getTask());

			service.execute(execution);
		}
		catch (Exception exception)
		{
			logger.error(exception.getMessage());
			fail();
		}
	}

	private Task getTask()
	{
		Task task = new Task();

		task.getRestriction().addRecipient().getIdentifier().setSystem(NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER)
				.setValue("MeDIC");

		return task;
	}
}
