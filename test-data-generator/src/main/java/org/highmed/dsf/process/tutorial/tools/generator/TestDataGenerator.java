package org.highmed.dsf.process.tutorial.tools.generator;

import java.nio.file.Path;
import java.util.Map;

import org.highmed.dsf.process.tutorial.tools.generator.CertificateGenerator.CertificateFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rwh.utils.crypto.CertificateAuthority;

public class TestDataGenerator
{
	private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);

	private static final CertificateGenerator certificateGenerator = new CertificateGenerator();
	private static final BundleGenerator bundleGenerator = new BundleGenerator();
	private static final EnvGenerator envGenerator = new EnvGenerator();

	static
	{
		CertificateAuthority.registerBouncyCastleProvider();
	}

	public static void main(String[] args)
	{
		certificateGenerator.generateCertificates();

		certificateGenerator.copyDockerTestClientCerts();
		certificateGenerator.copyDockerTestServerCert();

		Map<String, CertificateFiles> clientCertificateFilesByCommonName = certificateGenerator
				.getClientCertificateFilesByCommonName();

		CertificateFiles webbrowserTestUser = clientCertificateFilesByCommonName.get("Webbrowser Test User");
		Path p12File = certificateGenerator.createP12(webbrowserTestUser);
		logger.warn(
				"Install client-certificate and CA certificate from \"{}\" into your browsers certificate store to access fhir and bpe servers with your webbrowser",
				p12File.toAbsolutePath().toString());

		bundleGenerator.createDockerTestBundles(clientCertificateFilesByCommonName);
		bundleGenerator.copyDockerTestBundles();

		envGenerator.generateAndWriteDockerTestFhirEnvFiles(clientCertificateFilesByCommonName);
	}
}