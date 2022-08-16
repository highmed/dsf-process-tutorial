package org.highmed.dsf.process.tutorial.exercise_1.service;

import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_LEADING_TASK;
import static org.highmed.dsf.bpe.ConstantsBase.NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HelloDicServiceTest
{
	@Mock
	private DelegateExecution execution;

	@InjectMocks
	private HelloDic service;

	@Test
	public void testHelloDicServiceValid() throws Exception
	{
		Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK)).thenReturn(getTask());

		service.execute(execution);

		Mockito.verify(execution).getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK);
	}

	private Task getTask()
	{
		Task task = new Task();
		task.getRestriction().addRecipient().getIdentifier().setSystem(NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER)
				.setValue("MeDIC");

		return task;
	}
}
