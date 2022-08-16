package org.highmed.dsf.process.tutorial.exercise_4;

import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME;
import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI_AND_LATEST_VERSION;
import static org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition.VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;
import org.highmed.dsf.bpe.ProcessPluginDefinition;
import org.highmed.dsf.fhir.resources.ResourceProvider;
import org.highmed.dsf.process.tutorial.ConstantsTutorial;
import org.highmed.dsf.process.tutorial.TutorialProcessPluginDefinition;
import org.highmed.dsf.process.tutorial.message.HelloCosMessage;
import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.hl7.fhir.r4.model.ActivityDefinition;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.Test;
import org.springframework.core.env.StandardEnvironment;

import ca.uhn.fhir.context.FhirContext;

public class TutorialProcessPluginDefinitionTest
{
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

		String errorServiceTask = "Process '" + processId + "' in file '" + filename
				+ "' is missing implementation of class '" + HelloDic.class.getName() + "'";
		assertTrue(errorServiceTask, processes.get(0).getChildElementsByType(ServiceTask.class).stream()
				.filter(Objects::nonNull).map(ServiceTask::getCamundaClass).anyMatch(HelloDic.class.getName()::equals));

		List<MessageEventDefinition> messageEndEvent = processes.get(0).getChildElementsByType(EndEvent.class).stream()
				.filter(Objects::nonNull)
				.flatMap(e -> e.getChildElementsByType(MessageEventDefinition.class).stream().filter(Objects::nonNull))
				.collect(Collectors.toList());

		String errorMessageEndEvent = "Process '" + processId + "' in file '" + filename
				+ "' should end with a MessageEndEvent";
		assertEquals(errorMessageEndEvent, 1, messageEndEvent.size());

		String errorMessageEndEventImplementation = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent java implementation class '" + HelloCosMessage.class.getName() + "'";
		assertEquals(errorMessageEndEventImplementation, HelloCosMessage.class.getName(),
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
				+ PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI_AND_LATEST_VERSION + "'";
		assertTrue(errorMessageEndEventInputUri,
				inputParameters.stream().anyMatch(i -> "instantiatesUri".equals(i.getCamundaName())
						&& PROFILE_TUTORIAL_TASK_HELLO_COS_PROCESS_URI_AND_LATEST_VERSION.equals(i.getTextContent())));

		String errorMessageEndEventMessageName = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent input parameter with name 'messageName' and value '"
				+ PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME + "'";
		assertTrue(errorMessageEndEventMessageName,
				inputParameters.stream().anyMatch(i -> "messageName".equals(i.getCamundaName())
						&& PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME.equals(i.getTextContent())));

		String errorMessageEndEventProfile = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageEndEvent input parameter with name 'profile' and value '"
				+ PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION + "'";
		assertTrue(errorMessageEndEventProfile,
				inputParameters.stream().anyMatch(i -> "profile".equals(i.getCamundaName())
						&& PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION.equals(i.getTextContent())));

		List<SequenceFlow> gatewayOutgoingFlows = processes.get(0).getChildElementsByType(ExclusiveGateway.class)
				.stream().filter(Objects::nonNull).flatMap(g -> g.getOutgoing().stream()).collect(Collectors.toList());

		String errorGatewayOutgoingFlow = "Process '" + processId + "' in file '" + filename
				+ "' is missing an ExclusiveGateway with two outgoing sequence flows";
		assertEquals(errorGatewayOutgoingFlow, 2, gatewayOutgoingFlows.size());

		List<String> expressions = gatewayOutgoingFlows.stream().map(f -> f.getConditionExpression())
				.filter(Objects::nonNull).filter(e -> "bpmn:tFormalExpression".equals(e.getType()))
				.map(e -> e.getTextContent()).collect(Collectors.toList());
		String errorGatewayOutgoingFlowExpressions = "Process '" + processId + "' in file '" + filename
				+ "' is missing an ExclusiveGateway with two outgoing sequence flows having defined expression";
		assertEquals(errorGatewayOutgoingFlowExpressions, 2, expressions.size());

		String errorGatewayOutgoingFlowExpressionsSame = "Process '" + processId + "' in file '" + filename
				+ "' has ExclusiveGateway with two outgoing sequence flows having identical defined expression: '"
				+ expressions.get(0) + "'";
		assertNotEquals(errorGatewayOutgoingFlowExpressionsSame, expressions.get(0), expressions.get(1));
	}

	@Test
	public void testHelloDicResources() throws Exception
	{
		String codeSystemUrl = "http://highmed.org/fhir/CodeSystem/tutorial";
		String codeSystemCode = "tutorial-input";
		String valueSetUrl = "http://highmed.org/fhir/ValueSet/tutorial";

		ProcessPluginDefinition definition = new TutorialProcessPluginDefinition();
		ResourceProvider provider = definition.getResourceProvider(FhirContext.forR4(), getClass().getClassLoader(),
				new StandardEnvironment());
		assertNotNull(provider);

		List<MetadataResource> helloDic = provider.getResources(
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC + "/" + TutorialProcessPluginDefinition.VERSION,
				s -> ResourceProvider.empty()).collect(Collectors.toList());

		String errorCodeSystem = "Process is missing CodeSystem with url '" + codeSystemUrl + "' and concept '"
				+ codeSystemCode + "' with type 'string'";
		assertEquals(errorCodeSystem, 1, helloDic.stream().filter(r -> r instanceof CodeSystem).map(r -> (CodeSystem) r)
				.filter(c -> codeSystemUrl.equals(c.getUrl()))
				.filter(c -> c.getConcept().stream().anyMatch(con -> codeSystemCode.equals(con.getCode()))).count());

		String errorValueSet = "Process is missing ValueSet with url '" + valueSetUrl + "'";
		assertEquals(errorValueSet, 1, helloDic.stream().filter(r -> r instanceof ValueSet).map(r -> (ValueSet) r)
				.filter(v -> valueSetUrl.equals(v.getUrl()))
				.filter(v -> v.getCompose().getInclude().stream().anyMatch(i -> codeSystemUrl.equals(i.getSystem())))
				.count());

		assertEquals(4, helloDic.size());
	}

	@Test
	public void testHelloCosBpmnProcessFile() throws Exception
	{
		String filename = "bpe/hello-cos.bpmn";
		String processId = "highmedorg_helloCos";

		boolean cosProcessConfigured = new TutorialProcessPluginDefinition().getBpmnFiles()
				.anyMatch(f -> filename.equals(f));
		assertTrue("Process '" + processId + "' from file '" + filename + "' not configured in "
				+ TutorialProcessPluginDefinition.class.getSimpleName(), cosProcessConfigured);

		BpmnModelInstance model = Bpmn
				.readModelFromStream(this.getClass().getClassLoader().getResourceAsStream(filename));
		assertNotNull(model);

		List<Process> processes = model.getModelElementsByType(Process.class).stream()
				.filter(p -> processId.equals(p.getId())).collect(Collectors.toList());
		assertEquals(1, processes.size());

		List<MessageEventDefinition> messageStartEvent = processes.get(0).getChildElementsByType(StartEvent.class)
				.stream().filter(Objects::nonNull)
				.flatMap(e -> e.getChildElementsByType(MessageEventDefinition.class).stream().filter(Objects::nonNull))
				.collect(Collectors.toList());

		String errorStartEvent = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageStartEvent";
		assertEquals(errorStartEvent, 1, messageStartEvent.size());

		String errorStartEventMessageName = "Process '" + processId + "' in file '" + filename
				+ "' is missing a MessageStartEvent with message name 'helloCos'";
		assertEquals(errorStartEventMessageName, "helloCos", messageStartEvent.get(0).getMessage().getName());
	}

	@Test
	public void testHelloCosResources() throws Exception
	{
		ProcessPluginDefinition definition = new TutorialProcessPluginDefinition();
		ResourceProvider provider = definition.getResourceProvider(FhirContext.forR4(), getClass().getClassLoader(),
				new StandardEnvironment());
		assertNotNull(provider);

		List<MetadataResource> helloCos = provider.getResources(
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_COS + "/" + TutorialProcessPluginDefinition.VERSION,
				s -> ResourceProvider.empty()).collect(Collectors.toList());

		String processUrl = "http://highmed.org/bpe/Process/helloCos";
		List<ActivityDefinition> activityDefinitions = helloCos.stream().filter(r -> r instanceof ActivityDefinition)
				.map(r -> (ActivityDefinition) r).filter(a -> processUrl.equals(a.getUrl()))
				.filter(a -> VERSION.equals(a.getVersion())).collect(Collectors.toList());

		String errorActivityDefinition = "Process is missing ActivityDefinition with url '" + processUrl
				+ "' and version '" + VERSION + "'";
		assertEquals(errorActivityDefinition, 1, activityDefinitions.size());

		String errorMessageRequester = "ActivityDefinition with url '" + processUrl + "' and version '" + VERSION
				+ "' is missing expected requester extension";
		assertEquals(errorMessageRequester, 1, activityDefinitions.get(0).getExtension().stream()
				.filter(e -> "http://highmed.org/fhir/StructureDefinition/extension-process-authorization"
						.equals(e.getUrl()))
				.flatMap(e -> e.getExtension().stream()).filter(e -> "requester".equals(e.getUrl()))
				.map(Extension::getValue).filter(v -> v instanceof Coding).map(v -> (Coding) v)
				.filter(c -> "http://highmed.org/fhir/CodeSystem/process-authorization".equals(c.getSystem()))
				.filter(c -> "REMOTE_ORGANIZATION".equals(c.getCode())).flatMap(c -> c.getExtension().stream())
				.filter(e -> "http://highmed.org/fhir/StructureDefinition/extension-process-authorization-organization"
						.equals(e.getUrl()))
				.map(Extension::getValue).filter(v -> v instanceof Identifier).map(i -> (Identifier) i)
				.filter(i -> "http://highmed.org/sid/organization-identifier".equals(i.getSystem()))
				.filter(i -> "Test_DIC".equals(i.getValue())).count());

		String errorMessageRecipient = "ActivityDefinition with url '" + processUrl + "' and version '" + VERSION
				+ "' is missing expected recipient extension";
		assertEquals(errorMessageRecipient, 1, activityDefinitions.get(0).getExtension().stream()
				.filter(e -> "http://highmed.org/fhir/StructureDefinition/extension-process-authorization"
						.equals(e.getUrl()))
				.flatMap(e -> e.getExtension().stream()).filter(e -> "recipient".equals(e.getUrl()))
				.map(Extension::getValue).filter(v -> v instanceof Coding).map(v -> (Coding) v)
				.filter(c -> "http://highmed.org/fhir/CodeSystem/process-authorization".equals(c.getSystem()))
				.filter(c -> "LOCAL_ORGANIZATION".equals(c.getCode())).flatMap(c -> c.getExtension().stream())
				.filter(e -> "http://highmed.org/fhir/StructureDefinition/extension-process-authorization-organization"
						.equals(e.getUrl()))
				.map(Extension::getValue).filter(v -> v instanceof Identifier).map(i -> (Identifier) i)
				.filter(i -> "http://highmed.org/sid/organization-identifier".equals(i.getSystem()))
				.filter(i -> "Test_COS".equals(i.getValue())).count());

		String taskHelloCosUrl = "http://highmed.org/fhir/StructureDefinition/task-hello-cos";
		List<StructureDefinition> structureDefinitions = helloCos.stream().filter(r -> r instanceof StructureDefinition)
				.map(r -> (StructureDefinition) r).filter(s -> taskHelloCosUrl.equals(s.getUrl()))
				.filter(s -> VERSION.equals(s.getVersion())).collect(Collectors.toList());

		String errorStructureDefinition = "Process is missing StructureDefinition with url '" + taskHelloCosUrl
				+ "' and version '" + VERSION + "'";
		assertEquals(errorStructureDefinition, 1, structureDefinitions.size());

		assertEquals(2, helloCos.size());
	}
}
