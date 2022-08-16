package org.highmed.dsf.process.tutorial.exercise_2.spring.config;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.highmed.dsf.process.tutorial.spring.config.TutorialConfig;
import org.highmed.dsf.tools.generator.ProcessDocumentation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TutorialConfigTest
{
	@Test
	public void testDicServiceBeanDefined() throws Exception
	{
		long count = Arrays.stream(TutorialConfig.class.getMethods())
				.filter(m -> HelloDic.class.equals(m.getReturnType())).filter(m -> Modifier.isPublic(m.getModifiers()))
				.filter(m -> m.getAnnotation(Bean.class) != null).count();

		String errorMethod = "One public spring bean methods with return type " + HelloDic.class.getSimpleName()
				+ " and annotation " + Bean.class.getSimpleName() + " expected in "
				+ TutorialConfig.class.getSimpleName();
		assertEquals(errorMethod, 1, count);
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
}
