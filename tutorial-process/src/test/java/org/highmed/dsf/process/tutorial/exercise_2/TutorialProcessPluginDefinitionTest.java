package org.highmed.dsf.process.tutorial.exercise_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.highmed.dsf.bpe.ProcessPluginDefinition;
import org.highmed.dsf.fhir.resources.ResourceProvider;
import org.highmed.dsf.process.tutorial.ConstantsTutorial;
import org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.Test;
import org.springframework.core.env.StandardEnvironment;

import ca.uhn.fhir.context.FhirContext;

public class TutorialProcessPluginDefinitionTest
{
	@Test
	public void testBpmnProcessFile() throws Exception
	{
		String filename = "bpe/hello-dic.bpmn";
		String processId = "highmedorg_helloDic";
		String classname = "org.highmed.dsf.process.tutorial.service.HelloDic";

		BpmnModelInstance model = Bpmn
				.readModelFromStream(this.getClass().getClassLoader().getResourceAsStream(filename));
		assertNotNull(model);

		List<Process> processes = model.getModelElementsByType(Process.class).stream()
				.filter(p -> processId.equals(p.getId())).collect(Collectors.toList());
		assertEquals(1, processes.size());

		String errorServiceTask = "Process '" + processId + "' in file '" + filename
				+ "' is missing implementation of class '" + classname + "'";
		assertTrue(errorServiceTask, processes.get(0).getChildElementsByType(ServiceTask.class).stream()
				.filter(Objects::nonNull).map(ServiceTask::getCamundaClass).anyMatch(classname::equals));
	}

	@Test
	public void testProcessPluginDefinition() throws Exception
	{
		ProcessPluginDefinition definition = new TutorialProcessPluginDefinition();
		ResourceProvider provider = definition.getResourceProvider(FhirContext.forR4(), getClass().getClassLoader(),
				new StandardEnvironment());
		assertNotNull(provider);

		List<MetadataResource> helloDic = provider.getResources(
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC + "/" + TutorialProcessPluginDefinition.VERSION,
				s -> ResourceProvider.empty()).collect(Collectors.toList());

		assertEquals(4, helloDic.size());
		assertEquals("Process is missing CodeSystem with url 'http://highmed.org/fhir/CodeSystem/tutorial'", 1,
				helloDic.stream().filter(r -> r instanceof CodeSystem).count());
		assertEquals("Process is missing ValueSet with url 'http://highmed.org/fhir/ValueSet/tutorial'", 1,
				helloDic.stream().filter(r -> r instanceof ValueSet).count());
	}
}
