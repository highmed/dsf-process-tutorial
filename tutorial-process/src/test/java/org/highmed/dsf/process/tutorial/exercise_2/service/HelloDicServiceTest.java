package org.highmed.dsf.process.tutorial.exercise_2.service;

import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_LEADING_TASK;
import static org.highmed.dsf.bpe.ConstantsBase.NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.highmed.dsf.process.tutorial.spring.config.TutorialConfig;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

@RunWith(MockitoJUnitRunner.class)
public class HelloDicServiceTest
{
	@Mock
	private DelegateExecution execution;

	@InjectMocks
	private HelloDic service;

	@Test
	public void testHelloDicServiceAvailable()
	{
		long count = Arrays.stream(TutorialConfig.class.getMethods())
				.filter(m -> m.getReturnType().equals(HelloDic.class)).filter(m -> Modifier.isPublic(m.getModifiers()))
				.count();

		String errorMethod = "Configuration file 'TutorialConfig.java' contains " + count
				+ " public spring bean methods with return type 'HelloDic', expected 1";
		assertEquals(errorMethod, 1, count);
	}

	@Test
	public void testLogParameterAvailable()
	{
		String fieldname = "loggingEnabled";

		long count = Arrays.stream(TutorialConfig.class.getDeclaredFields()).filter(f -> fieldname.equals(f.getName()))
				.filter(f -> f.getAnnotation(Value.class) != null).filter(f -> Modifier.isPrivate(f.getModifiers()))
				.count();

		String errorMethod = "Configuration file 'TutorialConfig.java' contains " + count
				+ " private boolean members with name '" + fieldname
				+ "' and annotations [@Value, @ProcessDocumentation], expected 1";

		assertEquals(errorMethod, 1, count);
	}

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
