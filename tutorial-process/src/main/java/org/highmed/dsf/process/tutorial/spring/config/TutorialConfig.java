package org.highmed.dsf.process.tutorial.spring.config;

import org.highmed.dsf.fhir.authorization.read.ReadAccessHelper;
import org.highmed.dsf.fhir.client.FhirWebserviceClientProvider;
import org.highmed.dsf.fhir.task.TaskHelper;
import org.highmed.dsf.process.tutorial.service.HelloWorld;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Bean
	public HelloWorld helloWorld()
	{
		return new HelloWorld(clientProvider, taskHelper, readAccessHelper);
	}
}
