package org.highmed.dsf.process.tutorial.exercise_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.highmed.dsf.bpe.ProcessPluginDefinition;
import org.highmed.dsf.fhir.resources.ResourceProvider;
import org.highmed.dsf.process.tutorial.ConstantsTutorial;
import org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition;
import org.junit.Test;
import org.springframework.core.env.StandardEnvironment;

import ca.uhn.fhir.context.FhirContext;

public class TutorialProcessPluginDefinitionTest
{
	@Test
	public void testResourceLoading() throws Exception
	{
		ProcessPluginDefinition definition = new TutorialProcessPluginDefinition();
		ResourceProvider provider = definition.getResourceProvider(FhirContext.forR4(), getClass().getClassLoader(),
				new StandardEnvironment());
		assertNotNull(provider);

		var helloDic = provider.getResources(
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC + "/" + TutorialProcessPluginDefinition.VERSION,
				s -> ResourceProvider.empty());
		assertNotNull(helloDic);
		assertEquals(2, helloDic.count());
	}
}