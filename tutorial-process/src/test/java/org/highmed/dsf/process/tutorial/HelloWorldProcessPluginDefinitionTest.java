package org.highmed.dsf.process.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.highmed.dsf.bpe.ProcessPluginDefinition;
import org.highmed.dsf.fhir.resources.ResourceProvider;
import org.junit.Test;
import org.springframework.core.env.StandardEnvironment;

import ca.uhn.fhir.context.FhirContext;

public class HelloWorldProcessPluginDefinitionTest
{
	@Test
	public void testResourceLoading() throws Exception
	{
		ProcessPluginDefinition definition = new TutorialProcessPluginDefinition();
		ResourceProvider provider = definition.getResourceProvider(FhirContext.forR4(), getClass().getClassLoader(),
				new StandardEnvironment());
		assertNotNull(provider);

		var helloWorld = provider.getResources(
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC + "/" + TutorialProcessPluginDefinition.VERSION,
				s -> ResourceProvider.empty());
		assertNotNull(helloWorld);
		assertEquals(2, helloWorld.count());
	}
}
