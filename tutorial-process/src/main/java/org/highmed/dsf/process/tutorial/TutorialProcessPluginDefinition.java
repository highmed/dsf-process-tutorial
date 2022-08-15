package org.highmed.dsf.process.tutorial;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.highmed.dsf.bpe.ProcessPluginDefinition;
import org.highmed.dsf.fhir.resources.AbstractResource;
import org.highmed.dsf.fhir.resources.ActivityDefinitionResource;
import org.highmed.dsf.fhir.resources.ResourceProvider;
import org.highmed.dsf.fhir.resources.StructureDefinitionResource;
import org.highmed.dsf.process.tutorial.spring.config.TutorialConfig;
import org.springframework.core.env.PropertyResolver;

import ca.uhn.fhir.context.FhirContext;

public class TutorialProcessPluginDefinition implements ProcessPluginDefinition
{
	public static final String VERSION = "0.1.0";
	public static final LocalDate RELEASE_DATE = LocalDate.of(2022, 8, 21);

	@Override
	public String getName()
	{
		return "dsf-process-tutorial";
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public LocalDate getReleaseDate()
	{
		return RELEASE_DATE;
	}

	@Override
	public Stream<String> getBpmnFiles()
	{
		return Stream.of("bpe/hello-dic.bpmn");
	}

	@Override
	public Stream<Class<?>> getSpringConfigClasses()
	{
		return Stream.of(TutorialConfig.class);
	}

	@Override
	public ResourceProvider getResourceProvider(FhirContext fhirContext, ClassLoader classLoader,
			PropertyResolver resolver)
	{
		var aHelloProcess = ActivityDefinitionResource.file("fhir/ActivityDefinition/hello-dic.xml");
		var tHelloProcess = StructureDefinitionResource.file("fhir/StructureDefinition/task-hello-dic.xml");

		Map<String, List<AbstractResource>> resourcesByProcessKeyAndVersion = Map.of(
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC + "/" + VERSION,
				Arrays.asList(aHelloProcess, tHelloProcess));

		return ResourceProvider.read(VERSION, RELEASE_DATE,
				() -> fhirContext.newXmlParser().setStripVersionsFromReferences(false), classLoader, resolver,
				resourcesByProcessKeyAndVersion);
	}
}
