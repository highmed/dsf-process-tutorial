package org.highmed.dsf.process.tutorial.spring.config;

import static org.highmed.dsf.process.tutorial.ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC;

import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.highmed.dsf.process.tutorial.service.HelloDic;
import org.highmed.dsf.tools.generator.ProcessDocumentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TutorialConfig
{
	@Autowired
	private FhirWebserviceClientProvider clientProvider;

	@Autowired
	private TaskHelper taskHelper;

	@Autowired
	private ReadAccessHelper readAccessHelper;

	@Value("${org.highmed.dsf.process.tutorial.loggingEnabled:false}")
	@ProcessDocumentation(description = "Set to true to enable logging", required = false, processNames = PROCESS_NAME_FULL_HELLO_DIC)
	private boolean loggingEnabled;

	@Bean
	public HelloDic helloDic()
	{
		return new HelloDic(clientProvider, taskHelper, readAccessHelper, loggingEnabled);
	}
}
