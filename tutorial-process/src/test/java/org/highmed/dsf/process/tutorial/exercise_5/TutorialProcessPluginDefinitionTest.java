package org.highmed.dsf.process.tutorial.exercise_5;

import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_HRP;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_HRP_MESSAGE_NAME;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_HRP_PROCESS_URI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.EventBasedGateway;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.IntermediateThrowEvent;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.TimerEventDefinition;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;
import org.highmed.dsf.fhir.resources.ResourceProvider;
import org.highmed.dsf.process.tutorial.ConstantsTutorial;
import org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition;
import org.highmed.dsf.process.tutorial.message.HelloHrpMessage;
import org.highmed.dsf.process.tutorial.service.HelloCos;
import org.hl7.fhir.r4.model.ActivityDefinition;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.Test;
import org.springframework.core.env.StandardEnvironment;

import ca.uhn.fhir.context.FhirContext;

public class TutorialProcessPluginDefinitionTest
{
	private TutorialProcessPluginDefinition definition = new TutorialProcessPluginDefinition();

	@Test
	public void testGetBpmnFiles() throws Exception
	{
		var bpmnFiles = definition.getBpmnFiles();
		assertNotNull(bpmnFiles);
		assertEquals(3, bpmnFiles.count());
	}

	@Test
	public void testGetBpmnFilesCos() throws Exception
	{
		var bpmnFiles = definition.getBpmnFiles();
		assumeNotNull(bpmnFiles);
		assertTrue(bpmnFiles.anyMatch("bpe/hello-cos.bpmn"::equals));
	}

	@Test
	public void testGetBpmnFilesDic() throws Exception
	{
		var bpmnFiles = definition.getBpmnFiles();
		assumeNotNull(bpmnFiles);
		assertTrue(bpmnFiles.anyMatch("bpe/hello-dic.bpmn"::equals));
	}

	@Test
	public void testGetBpmnFilesHrp() throws Exception
	{
		var bpmnFiles = definition.getBpmnFiles();
		assumeNotNull(bpmnFiles);
		assertTrue(bpmnFiles.anyMatch("bpe/hello-hrp.bpmn"::equals));
	}

	@Test
	public void testGetResourceProvider() throws Exception
	{
		var provider = definition.getResourceProvider(FhirContext.forR4(), getClass().getClassLoader(),
				new StandardEnvironment());
		assertNotNull(provider);
	}

	private List<MetadataResource> getResources(String processKey)
	{
		var provider = definition.getResourceProvider(FhirContext.forR4(), getClass().getClassLoader(),
				new StandardEnvironment());
		assumeNotNull(provider);

		return provider
				.getResources(processKey + "/" + TutorialProcessPluginDefinition.VERSION, s -> ResourceProvider.empty())
				.collect(Collectors.toList());
	}

	@Test
	public void testGetResourceProviderCos() throws Exception
	{
		var resources = getResources(ConstantsTutorial.PROCESS_NAME_FULL_HELLO_COS);
		assertNotNull(resources);
		assertEquals(4, resources.size());

		long aCount = resources.stream().filter(r -> r instanceof ActivityDefinition).map(r -> (ActivityDefinition) r)
				.filter(a -> "http://highmed.org/bpe/Process/helloCos".equals(a.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(a.getVersion()))
				.count();
		assertEquals(1, aCount);

		long cCount = resources.stream().filter(r -> r instanceof CodeSystem).map(r -> (CodeSystem) r)
				.filter(c -> "http://highmed.org/fhir/CodeSystem/tutorial".equals(c.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(c.getVersion()))
				.count();
		assertEquals(1, cCount);

		long tCount = resources.stream().filter(r -> r instanceof StructureDefinition).map(r -> (StructureDefinition) r)
				.filter(c -> "http://highmed.org/fhir/StructureDefinition/task-hello-cos".equals(c.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(c.getVersion()))
				.count();
		assertEquals(1, tCount);

		long vCount = resources.stream().filter(r -> r instanceof ValueSet).map(r -> (ValueSet) r)
				.filter(v -> "http://highmed.org/fhir/ValueSet/tutorial".equals(v.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(v.getVersion()))
				.count();
		assertEquals(1, vCount);
	}

	@Test
	public void testGetResourceProviderDic() throws Exception
	{
		var resources = getResources(ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC);
		assertNotNull(resources);
		assertEquals(5, resources.size());

		long aCount = resources.stream().filter(r -> r instanceof ActivityDefinition).map(r -> (ActivityDefinition) r)
				.filter(a -> "http://highmed.org/bpe/Process/helloDic".equals(a.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(a.getVersion()))
				.count();
		assertEquals(1, aCount);

		long cCount = resources.stream().filter(r -> r instanceof CodeSystem).map(r -> (CodeSystem) r)
				.filter(c -> "http://highmed.org/fhir/CodeSystem/tutorial".equals(c.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(c.getVersion()))
				.count();
		assertEquals(1, cCount);

		long t1Count = resources.stream().filter(r -> r instanceof StructureDefinition)
				.map(r -> (StructureDefinition) r)
				.filter(c -> "http://highmed.org/fhir/StructureDefinition/task-goodbye-dic".equals(c.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(c.getVersion()))
				.count();
		assertEquals(1, t1Count);

		long t2Count = resources.stream().filter(r -> r instanceof StructureDefinition)
				.map(r -> (StructureDefinition) r)
				.filter(c -> "http://highmed.org/fhir/StructureDefinition/task-hello-dic".equals(c.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(c.getVersion()))
				.count();
		assertEquals(1, t2Count);

		long vCount = resources.stream().filter(r -> r instanceof ValueSet).map(r -> (ValueSet) r)
				.filter(v -> "http://highmed.org/fhir/ValueSet/tutorial".equals(v.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(v.getVersion()))
				.count();
		assertEquals(1, vCount);
	}

	@Test
	public void testGetResourceProviderDicActivityDefinitionHelloDic() throws Exception
	{
		var resources = getResources(ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC);
		assumeNotNull(resources);

		var aOpt = resources.stream().filter(r -> r instanceof ActivityDefinition).map(r -> (ActivityDefinition) r)
				.filter(a -> "http://highmed.org/bpe/Process/helloDic".equals(a.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(a.getVersion()))
				.findFirst();
		assumeTrue(aOpt.isPresent());

		var a = aOpt.get();
		var pAuthExts = a
				.getExtensionsByUrl("http://highmed.org/fhir/StructureDefinition/extension-process-authorization");
		assertNotNull(pAuthExts);
		assertEquals(2, pAuthExts.size());
	}

	@Test
	public void testGetResourceProviderHrp() throws Exception
	{
		var resources = getResources(ConstantsTutorial.PROCESS_NAME_FULL_HELLO_HRP);
		assertNotNull(resources);
		assertEquals(4, resources.size());

		long aCount = resources.stream().filter(r -> r instanceof ActivityDefinition).map(r -> (ActivityDefinition) r)
				.filter(a -> "http://highmed.org/bpe/Process/helloHrp".equals(a.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(a.getVersion()))
				.count();
		assertEquals(1, aCount);

		long cCount = resources.stream().filter(r -> r instanceof CodeSystem).map(r -> (CodeSystem) r)
				.filter(c -> "http://highmed.org/fhir/CodeSystem/tutorial".equals(c.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(c.getVersion()))
				.count();
		assertEquals(1, cCount);

		long tCount = resources.stream().filter(r -> r instanceof StructureDefinition).map(r -> (StructureDefinition) r)
				.filter(c -> "http://highmed.org/fhir/StructureDefinition/task-hello-hrp".equals(c.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(c.getVersion()))
				.count();
		assertEquals(1, tCount);

		long vCount = resources.stream().filter(r -> r instanceof ValueSet).map(r -> (ValueSet) r)
				.filter(v -> "http://highmed.org/fhir/ValueSet/tutorial".equals(v.getUrl())
						&& TutorialProcessPluginDefinition.VERSION.equals(v.getVersion()))
				.count();
		assertEquals(1, vCount);
	}

	@Test
	public void testHelloDicBpmnProcessFile() throws Exception
	{
		String filename = "bpe/hello-dic.bpmn";
		String processId = "highmedorg_helloDic";

		BpmnModelInstance model = Bpmn
				.readModelFromStream(this.getClass().getClassLoader().getResourceAsStream(filename));
		assertNotNull(model);

		List<Process> processes = model.getModelElementsByType(Process.class).stream()
				.filter(p -> processId.equals(p.getId())).collect(Collectors.toList());
		assertEquals(1, processes.size());

		List<MessageEventDefinition> intermediateMessageThrowEvent = processes.get(0)
				.getChildElementsByType(IntermediateThrowEvent.class).stream().filter(Objects::nonNull)
				.flatMap(e -> e.getChildElementsByType(MessageEventDefinition.class).stream().filter(Objects::nonNull))
				.collect(Collectors.toList());

		String errorIntermediateMessageThrowEvent = "Process '" + processId + "' in file '" + filename
				+ "' is missing an IntermediateMessageThrowEvent";
		assertEquals(errorIntermediateMessageThrowEvent, 1, intermediateMessageThrowEvent.size());

		String errorIntermediateMessageThrowEventMessageName = "Process '" + processId + "' in file '" + filename
				+ "' is missing a IntermediateMessageThrowEvent with message name 'helloCos'";
		assertEquals(errorIntermediateMessageThrowEventMessageName, "helloCos",
				intermediateMessageThrowEvent.get(0).getMessage().getName());

		long eventBasedGatewayCount = processes.get(0).getChildElementsByType(EventBasedGateway.class).stream()
				.filter(Objects::nonNull).count();
		String errorEventBasedGatewayCount = "Process '" + processId + "' in file '" + filename
				+ "' is missing an EventBasedGateway";
		assertEquals(errorEventBasedGatewayCount, 1, eventBasedGatewayCount);

		List<MessageEventDefinition> intermediateMessageCatchEvent = processes.get(0)
				.getChildElementsByType(IntermediateCatchEvent.class).stream().filter(Objects::nonNull)
				.flatMap(e -> e.getChildElementsByType(MessageEventDefinition.class).stream().filter(Objects::nonNull))
				.collect(Collectors.toList());

		String errorIntermediateMessageCatchEvent = "Process '" + processId + "' in file '" + filename
				+ "' is missing an IntermediateMessageCatchEvent";
		assertEquals(errorIntermediateMessageCatchEvent, 1, intermediateMessageCatchEvent.size());

		String errorIntermediateMessageCatchEventMessageName = "Process '" + processId + "' in file '" + filename
				+ "' is missing a IntermediateMessageCatchEvent with message name 'goodbyeDic'";
		assertEquals(errorIntermediateMessageCatchEventMessageName, "goodbyeDic",
				intermediateMessageCatchEvent.get(0).getMessage().getName());

		List<TimerEventDefinition> intermediateTimerCatchEvent = processes.get(0)
				.getChildElementsByType(IntermediateCatchEvent.class).stream().filter(Objects::nonNull)
				.flatMap(e -> e.getChildElementsByType(TimerEventDefinition.class).stream().filter(Objects::nonNull))
				.collect(Collectors.toList());

		String errorIntermediateTimerCatchEvent = "Process '" + processId + "' in file '" + filename
				+ "' is missing an IntermediateTimerCatchEvent";
		assertEquals(errorIntermediateTimerCatchEvent, 1, intermediateTimerCatchEvent.size());

		assertNotNull(intermediateTimerCatchEvent.get(0).getTimeDuration());
		assertNotNull(intermediateTimerCatchEvent.get(0).getTimeDuration().getTextContent());
		String errorIntermediateTimerCatchEventTimerDefinition = "Process '" + processId + "' in file '" + filename
				+ "' is missing a IntermediateTimerCatchEvent with timer definition";
		assertTrue(errorIntermediateTimerCatchEventTimerDefinition,
				intermediateTimerCatchEvent.get(0).getTimeDuration().getTextContent().startsWith("P"));
	}

	@Test
	public void testHelloCosBpmnProcessFile() throws Exception
	{
		String filename = "bpe/hello-cos.bpmn";
		String processId = "highmedorg_helloCos";

		BpmnModelInstance model = Bpmn
				.readModelFromStream(this.getClass().getClassLoader().getResourceAsStream(filename));
		assertNotNull(model);

		List<Process> processes = model.getModelElementsByType(Process.class).stream()
				.filter(p -> processId.equals(p.getId())).collect(Collectors.toList());
		assertEquals(1, processes.size());

		String errorServiceTask = "Process '" + processId + "' in file '" + filename
				+ "' is missing implementation of class '" + HelloCos.class.getName() + "'";
		assertTrue(errorServiceTask, processes.get(0).getChildElementsByType(ServiceTask.class).stream()
				.filter(Objects::nonNull).map(ServiceTask::getCamundaClass).anyMatch(HelloCos.class.getName()::equals));

		List<MessageEventDefinition> messageEndEvent = processes.get(0).getChildElementsByType(EndEvent.class).stream()
				.filter(Objects::nonNull)
				.flatMap(e -> e.getChildElementsByType(MessageEventDefinition.class).stream().filter(Objects::nonNull))
				.collect(Collectors.toList());

		String errorMessageEndEvent = "Process '" + processId + "' in file '" + filename
				+ "' should end with a MessageEndEvent";
		assertEquals(errorMessageEndEvent, 1, messageEndEvent.size());

		String errorMessageEndEventImplementation = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent java implementation class '" + HelloHrpMessage.class.getName() + "'";
		assertEquals(errorMessageEndEventImplementation, HelloHrpMessage.class.getName(),
				messageEndEvent.get(0).getCamundaClass());

		List<CamundaInputParameter> inputParameters = processes.get(0).getChildElementsByType(EndEvent.class).stream()
				.findAny().stream().flatMap(e -> e.getChildElementsByType(ExtensionElements.class).stream())
				.flatMap(e -> e.getChildElementsByType(CamundaInputOutput.class).stream().filter(Objects::nonNull))
				.flatMap(in -> in.getChildElementsByType(CamundaInputParameter.class).stream().filter(Objects::nonNull))
				.collect(Collectors.toList());

		String errorMessageEndEventInputs = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent with 3 input parameters";
		assertEquals(errorMessageEndEventInputs, 3, inputParameters.size());

		String errorMessageEndEventInputUri = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent input parameter with name 'instantiatesUri' and value '"
				+ PROFILE_TUTORIAL_TASK_HELLO_HRP_PROCESS_URI + "#{version}'";
		assertTrue(errorMessageEndEventInputUri,
				inputParameters.stream().anyMatch(i -> "instantiatesUri".equals(i.getCamundaName())
						&& (PROFILE_TUTORIAL_TASK_HELLO_HRP_PROCESS_URI + "#{version}").equals(i.getTextContent())));

		String errorMessageEndEventMessageName = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent input parameter with name 'messageName' and value '"
				+ PROFILE_TUTORIAL_TASK_HELLO_HRP_MESSAGE_NAME + "'";
		assertTrue(errorMessageEndEventMessageName,
				inputParameters.stream().anyMatch(i -> "messageName".equals(i.getCamundaName())
						&& PROFILE_TUTORIAL_TASK_HELLO_HRP_MESSAGE_NAME.equals(i.getTextContent())));

		String errorMessageEndEventProfile = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent input parameter with name 'profile' and value '"
				+ PROFILE_TUTORIAL_TASK_HELLO_HRP + "|#{version}'";
		assertTrue(errorMessageEndEventProfile,
				inputParameters.stream().anyMatch(i -> "profile".equals(i.getCamundaName())
						&& (PROFILE_TUTORIAL_TASK_HELLO_HRP + "|#{version}").equals(i.getTextContent())));
	}

	@Test
	public void testHelloHrpBpmnProcessFile() throws Exception
	{
		String filename = "bpe/hello-hrp.bpmn";
		String processId = "highmedorg_helloHrp";

		BpmnModelInstance model = Bpmn
				.readModelFromStream(this.getClass().getClassLoader().getResourceAsStream(filename));
		assertNotNull(model);

		List<Process> processes = model.getModelElementsByType(Process.class).stream()
				.filter(p -> processId.equals(p.getId())).collect(Collectors.toList());

		String errorProcessDefinitionKey = "Process in file '" + filename + "' is missing process definition key '"
				+ processId + "'";
		assertEquals(errorProcessDefinitionKey, 1, processes.size());

		String errorProcessVersion = "Process '" + processId + "' in file '" + filename
				+ "' is missing version tag '#{version}'";
		assertEquals(errorProcessVersion, "#{version}", processes.get(0).getCamundaVersionTag());
	}
}