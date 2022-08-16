package org.highmed.dsf.process.tutorial.exercise_1.spring.config;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.highmed.dsf.process.tutorial.spring.config.TutorialConfig;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

public class TutorialConfigTest
{
	@Test
	public void testDicServiceBeanDefined() throws Exception
	{
		long count = Arrays.stream(TutorialConfig.class.getMethods())
				.filter(m -> HelloDic.class.equals(m.getReturnType())).filter(m -> Modifier.isPublic(m.getModifiers()))
				.filter(m -> m.getAnnotation(Bean.class) != null).count();

		String errorMethod = "Configuration class " + TutorialConfig.class.getSimpleName() + " contains " + count
				+ " public spring bean methods with return type " + HelloDic.class.getSimpleName() + ", expected 1";
		assertEquals(errorMethod, 1, count);
	}
}
