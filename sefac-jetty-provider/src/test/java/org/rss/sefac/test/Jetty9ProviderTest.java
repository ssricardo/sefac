package org.rss.sefac.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.rss.sefac.Server;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.BasicConfiguration;
import org.rss.sefac.spi.Jetty9Provider;
import org.rss.sefac.spi.ServerProvider;

public class Jetty9ProviderTest {

	@Test
	public void testRunOK() throws Exception {
		BasicConfiguration config = new BasicConfiguration();
		Files.createDirectories(Paths.get(".", "target/jetty"));
		config.setServerDir(Paths.get(".", "target/jetty"))
			.setPort(8080);

		Server server = Server.create(config);

		WebApp app = new WebApp("test");
		
		Path baseDir = Paths.get("target/test-classes" /*"srv/test/java"*/);
//		Files.createDirectories(baseDir);
		app.setSourceLocation(baseDir);
		app.setContextRoot("test");
		app.setWebApp(Paths.get("src/test/resources") /*baseDir*/);
		server.addApplication(app);

		server.start();
		Thread.sleep(1000);
		server.stop();
	}
	
	@Test
	public void testExternalApp() throws Exception {
		Path testApp = Paths.get("../simple-war-app/target/simple-war-app.war");
		
		if (! Files.exists(testApp)) {
			System.err.println("*** External file 'simple-war-app' was NOT found. Skipping test...");
			return;
		}
		
		BasicConfiguration config = new BasicConfiguration();
		Path serverDir = Paths.get(".", "target/jetty");
		Files.createDirectories(serverDir);
		config
			.setServerDir(serverDir)
			.setPort(8080);
		Server server = Server.create(config);
		
		WebApp app = new WebApp("simple-test");
		app.setContextRoot("/web");
		app.setWebApp(testApp);
		server.addApplication(app);
		
		server.start();
		Thread.sleep(1000);
		server.stop();
	}
	
	@Test
	public void testUnwrap() throws Exception {
		Jetty9Provider provider = new Jetty9Provider();
		BasicConfiguration config = new BasicConfiguration();
		config.setHost("localhost").setPort(3030)
			.setServerDir(Paths.get("target/glassfish"));
		provider.setConfiguration(config);
		
		assertNull(provider.unwrap(Math.class));
		assertNotNull(provider.unwrap(org.eclipse.jetty.server.Server.class));
		assertNotNull(provider.unwrap(ServerProvider.class));
	}
}
