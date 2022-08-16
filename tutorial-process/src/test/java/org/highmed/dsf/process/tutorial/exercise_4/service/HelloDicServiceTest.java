package org.highmed.dsf.process.tutorial.exercise_4.service;

import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_LEADING_TASK;
import static org.highmed.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_TARGET;
import static org.highmed.dsf.bpe.ConstantsBase.NAMINGSYSTEM_HIGHMED_ORGANIZATION_IDENTIFIER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.value.BooleanValue;
import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.highmed.dsf.fhir.variables.TargetValues.TargetValue;
import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HelloDicServiceTest
{
	@Mock
	private FhirWebserviceClientProvider clientProvider;

	@Mock
	private TaskHelper taskHelper;

	@Mock
	private ReadAccessHelper readAccessHelper;

	@Mock
	private DelegateExecution execution;

	private Optional<Constructor<HelloDic>> getConstructor(Class<?>... args)
	{
		try
		{
			return Optional.of(HelloDic.class.getConstructor(args));
		}
		catch (NoSuchMethodException e)
		{
			return Optional.empty();
		}
		catch (SecurityException e)
		{
			throw e;
		}
	}

	@Test
	public void testHelloDicConstructorWithAdditionalBooleanParameterExists() throws Exception
	{
		// not testing all parameter permutations
		Optional<Constructor<HelloDic>> constructor = getConstructor(FhirWebserviceClientProvider.class,
				TaskHelper.class, ReadAccessHelper.class, boolean.class);
		if (constructor.isEmpty())
			constructor = getConstructor(boolean.class, FhirWebserviceClientProvider.class, TaskHelper.class,
					ReadAccessHelper.class);
		if (constructor.isEmpty())
			constructor = getConstructor(FhirWebserviceClientProvider.class, boolean.class, TaskHelper.class,
					ReadAccessHelper.class);
		if (constructor.isEmpty())
			constructor = getConstructor(FhirWebserviceClientProvider.class, TaskHelper.class, boolean.class,
					ReadAccessHelper.class);

		if (constructor.isEmpty())
		{
			String errorMessage = "One public constructor in class " + HelloDic.class.getSimpleName()
					+ " with parameters (" + FhirWebserviceClientProvider.class.getSimpleName() + ", "
					+ TaskHelper.class.getSimpleName() + ", " + ReadAccessHelper.class.getSimpleName()
					+ ", boolean) expected";
			fail(errorMessage);
		}
	}

	private Optional<HelloDic> getInstance(List<Class<?>> types, Object... args)
	{
		try
		{
			return Optional.of(HelloDic.class.getConstructor(types.toArray(Class[]::new))).map(c ->
			{
				try
				{
					return c.newInstance(args);
				}
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e)
				{
					throw new RuntimeException(e);
				}
			});
		}
		catch (NoSuchMethodException e)
		{
			return Optional.empty();
		}
		catch (SecurityException e)
		{
			throw e;
		}
	}

	@Test
	public void testHelloDicServiceDoExecute() throws Exception
	{
		// not trying all parameter permutations
		Optional<HelloDic> optService = getInstance(Arrays.asList(FhirWebserviceClientProvider.class, TaskHelper.class,
				ReadAccessHelper.class, boolean.class), clientProvider, taskHelper, readAccessHelper, true);
		if (optService.isEmpty())
			optService = getInstance(Arrays.asList(boolean.class, FhirWebserviceClientProvider.class, TaskHelper.class,
					ReadAccessHelper.class), true, clientProvider, taskHelper, readAccessHelper);
		if (optService.isEmpty())
			optService = getInstance(Arrays.asList(FhirWebserviceClientProvider.class, boolean.class, TaskHelper.class,
					ReadAccessHelper.class), clientProvider, true, taskHelper, readAccessHelper);
		if (optService.isEmpty())
			optService = getInstance(Arrays.asList(FhirWebserviceClientProvider.class, TaskHelper.class, boolean.class,
					ReadAccessHelper.class), clientProvider, taskHelper, true, readAccessHelper);

		assumeTrue(optService.isPresent());

		Task task = getTask();
		Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK)).thenReturn(task);
		Mockito.when(taskHelper.getFirstInputParameterStringValue(any(),
				eq("http://highmed.org/fhir/CodeSystem/tutorial"), eq("tutorial-input")))
				.thenReturn(Optional.of("Test"));

		optService.get().execute(execution);

		ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
		Mockito.verify(taskHelper).getFirstInputParameterStringValue(captor.capture(),
				eq("http://highmed.org/fhir/CodeSystem/tutorial"), eq("tutorial-input"));
		Mockito.verify(execution, atLeastOnce()).getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK);
		assertEquals(task, captor.getValue());

		ArgumentCaptor<TargetValue> targetArgumentCaptor = ArgumentCaptor.forClass(TargetValue.class);
		Mockito.verify(execution).setVariable(eq(BPMN_EXECUTION_VARIABLE_TARGET), targetArgumentCaptor.capture());
		assertEquals("Test_COS", targetArgumentCaptor.getValue().getValue().getOrganizationIdentifierValue());
		assertEquals("Test_COS_Endpoint", targetArgumentCaptor.getValue().getValue().getEndpointIdentifierValue());
		assertEquals("https://cos/fhir", targetArgumentCaptor.getValue().getValue().getEndpointUrl());

		Mockito.verify(execution).setVariable(anyString(), any(BooleanValue.class));
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
