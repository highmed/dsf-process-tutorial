package org.highmed.dsf.process.tutorial.exercise_5.config;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.highmed.dsf.process.tutorial.message.GoodbyeDicMessage;
import org.highmed.dsf.process.tutorial.message.HelloCosMessage;
import org.highmed.dsf.process.tutorial.message.HelloHrpMessage;
import org.highmed.dsf.process.tutorial.service.HelloCos;
import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.highmed.dsf.process.tutorial.service.HelloHrp;
import org.highmed.dsf.process.tutorial.spring.config.TutorialConfig;
import org.highmed.dsf.tools.generator.ProcessDocumentation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TutorialConfigTest
{
	private long countBeanMethods(Class<?> returnType)
	{
		return Arrays.stream(TutorialConfig.class.getMethods()).filter(m -> returnType.equals(m.getReturnType()))
				.filter(m -> Modifier.isPublic(m.getModifiers())).filter(m -> m.getAnnotation(Bean.class) != null)
				.count();
	}

	private String errorMessageBeanMethod(Class<?> returnType)
	{
		return "One public spring bean method with return type " + returnType.getSimpleName() + " and annotation "
				+ Bean.class.getSimpleName() + " expected in " + TutorialConfig.class.getSimpleName();
	}

	@Test
	public void testHelloDicBeanDefined() throws Exception
	{
		assertEquals(errorMessageBeanMethod(HelloDic.class), 1, countBeanMethods(HelloDic.class));
	}

	@Test
	public void testHelloCosMessageBeanDefined() throws Exception
	{
		assertEquals(errorMessageBeanMethod(HelloCosMessage.class), 1, countBeanMethods(HelloCosMessage.class));
	}

	@Test
	public void testConfigParameterExists() throws Exception
	{
		long count = Arrays.stream(TutorialConfig.class.getDeclaredFields())
				.filter(f -> boolean.class.equals(f.getType()))
				.filter(f -> f.getAnnotationsByType(Value.class).length == 1)
				.filter(f -> f.getAnnotation(Value.class).value() != null).count();

		String errorMessage = "One private field of type boolean with " + Value.class.getName()
				+ " annotation including value expected in " + TutorialConfig.class.getSimpleName();
		assertEquals(errorMessage, 1, count);
	}

	@Test
	public void testConfigParameterDocumetationExists() throws Exception
	{
		long count = Arrays.stream(TutorialConfig.class.getDeclaredFields())
				.filter(f -> boolean.class.equals(f.getType()))
				.filter(f -> f.getAnnotationsByType(ProcessDocumentation.class).length == 1)
				.filter(f -> f.getAnnotation(ProcessDocumentation.class).description() != null).count();

		String errorMessage = "One private field of type boolean with " + ProcessDocumentation.class.getName()
				+ " annotation including description expected in " + TutorialConfig.class.getSimpleName();
		assertEquals(errorMessage, 1, count);
	}

	@Test
	public void testHelloCosBeanDefined() throws Exception
	{
		assertEquals(errorMessageBeanMethod(HelloCos.class), 1, countBeanMethods(HelloCos.class));
	}

	@Test
	public void testHelloHrpBeanDefined() throws Exception
	{
		assertEquals(errorMessageBeanMethod(HelloHrp.class), 1, countBeanMethods(HelloHrp.class));
	}

	@Test
	public void testHelloHrpMessageBeanDefined() throws Exception
	{
		assertEquals(errorMessageBeanMethod(HelloHrpMessage.class), 1, countBeanMethods(HelloHrpMessage.class));
	}

	@Test
	public void testGoodbyeDicMessageBeanDefined() throws Exception
	{
		assertEquals(errorMessageBeanMethod(GoodbyeDicMessage.class), 1, countBeanMethods(GoodbyeDicMessage.class));
	}
}
