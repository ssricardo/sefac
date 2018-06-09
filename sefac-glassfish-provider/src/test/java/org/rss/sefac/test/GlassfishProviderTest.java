package org.rss.sefac.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.glassfish.embeddable.GlassFish;
import org.junit.Test;
import org.rss.sefac.Server;
import org.rss.sefac.app.JarApplication;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.BasicConfiguration;
import org.rss.sefac.spi.Glassfish3Provider;
import org.rss.sefac.spi.ServerProvider;

public class GlassfishProviderTest {

	Glassfish3Provider provider = null;
	
	@Test(expected=IllegalStateException.class)
	public void testNoConfig() throws Exception {
		provider = new Glassfish3Provider();
		provider.addApplication(new WebApp("NoConfig"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConfig() throws Exception {
		provider = new Glassfish3Provider();
		provider.setConfiguration(null);
	}
	
	@Test
	public void testWebAppOK() throws Exception {
		provider = new Glassfish3Provider();
		BasicConfiguration config = new BasicConfiguration();
		config.setHost("localhost").setPort(3030)
			.setServerDir(Paths.get("target/glassfish"));
		WebApp app = new WebApp("appOk");
		app.setContextRoot("test");
		app.setWebApp(Paths.get("src/test/resources"));
		app.setSourceLocation(Paths.get("target", "classes"));
		provider.setConfiguration(config);
		provider.addApplication(app);
	}
	
	@Test
	public void testFullConfig() throws Exception {
		BasicConfiguration config = new BasicConfiguration();
		config.setHost("localhost").setPort(3030)
			.setServerDir(Paths.get("target", "glassfish"));
		WebApp app = new WebApp("fullTest");
		app.setWebApp(Paths.get("src/test/resources/web"));
		app.setContextRoot("test");
		app.setSourceLocation(Paths.get("target", "test-classes"));
		
		Server server = Server.create(config);
		server.addApplication(app);
		server.start();
		Thread.sleep(1000);
		server.stop();
	}
	
	@Test
	public void testJarProject() throws Exception {
		provider = new Glassfish3Provider();
		BasicConfiguration config = new BasicConfiguration();
		config.setPort(5555)
			.setServerDir(Paths.get("target", "glassfish"));
		JarApplication app = new JarApplication("jarTest");
		app.setSourceLocation(Paths.get("target", "test-classes"));
		
		provider.setConfiguration(config);
		provider.addApplication(app);
	}
	
	@Test
	public void testUnwrap() throws Exception {
		provider = new Glassfish3Provider();
		BasicConfiguration config = new BasicConfiguration();
		config.setHost("localhost").setPort(3030)
			.setServerDir(Paths.get("target/glassfish"));
		provider.setConfiguration(config);
		
		assertNull(provider.unwrap(Math.class));
		assertNotNull(provider.unwrap(GlassFish.class));
		assertNotNull(provider.unwrap(ServerProvider.class));
	}
	
	@Test
//	@Ignore("Depends on environment: referenced file")
	public void testExternalApp() throws Exception {
		Path testApp = Paths.get("../simple-war-app/target/simple-war-app.war");
		
		if (! Files.exists(testApp)) {
			System.err.println("*** External file 'simple-war-app' was NOT found. Skipping test...");
			return;
		}
		
		provider = new Glassfish3Provider();
		BasicConfiguration config = new BasicConfiguration();
		config.setPort(8080);
		provider.setConfiguration(config);
		WebApp app = new WebApp("simple-app");
		app.setSourceLocation(testApp);
		provider.addApplication(app);
		provider.start();
		Thread.sleep(1000);
		provider.stop();
	}
}
