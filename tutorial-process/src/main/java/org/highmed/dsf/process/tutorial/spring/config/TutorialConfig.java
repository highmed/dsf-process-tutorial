package org.highmed.dsf.process.tutorial.spring.config;

import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC;

import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.organization.OrganizationProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.highmed.dsf.process.tutorial.message.GoodbyeDicMessage;
import org.highmed.dsf.process.tutorial.message.HelloCosMessage;
import org.highmed.dsf.process.tutorial.message.HelloHrpMessage;
import org.highmed.dsf.process.tutorial.service.HelloCos;
import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.highmed.dsf.process.tutorial.service.HelloHrp;
import org.highmed.dsf.tools.generator.ProcessDocumentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;

@Configuration
public class TutorialConfig
{
	@Autowired
	private FhirWebserviceClientProvider clientProvider;

	@Autowired
	private TaskHelper taskHelper;

	@Autowired
	private ReadAccessHelper readAccessHelper;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private FhirContext fhirContext;

	@Value("${org.highmed.dsf.process.tutorial.loggingEnabled:false}")
	@ProcessDocumentation(description = "Set to true to enable logging", required = false, processNames = PROCESS_NAME_FULL_HELLO_DIC)
	private boolean loggingEnabled;

	@Bean
	public HelloDic helloDic()
	{
		return new HelloDic(clientProvider, taskHelper, readAccessHelper, loggingEnabled);
	}

	@Bean
	public HelloCosMessage helloCosMessage()
	{
		return new HelloCosMessage(clientProvider, taskHelper, readAccessHelper, organizationProvider, fhirContext);
	}

	@Bean
	public HelloCos helloCos()
	{
		return new HelloCos(clientProvider, taskHelper, readAccessHelper);
	}

	@Bean
	public HelloHrpMessage helloHrpMessage()
	{
		return new HelloHrpMessage(clientProvider, taskHelper, readAccessHelper, organizationProvider, fhirContext);
	}

	@Bean
	public HelloHrp helloHrp()
	{
		return new HelloHrp(clientProvider, taskHelper, readAccessHelper);
	}

	@Bean
	public GoodbyeDicMessage GoodbyeDicMessage()
	{
		return new GoodbyeDicMessage(clientProvider, taskHelper, readAccessHelper, organizationProvider, fhirContext);
	}
}
