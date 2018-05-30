package org.rss.sefac.test.web;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.catalina.startup.Tomcat;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.rss.sefac.Server;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.BasicConfiguration;
import org.rss.sefac.spi.Tomcat7Provider;

public class TomcatProviderTest {

	Tomcat7Provider provider = new Tomcat7Provider();
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConfig() throws Exception {
		provider.setConfiguration(null);
	}
	
	@Test
	public void testDefaultConfig() throws Exception {
		provider.setConfiguration(new BasicConfiguration());
		Tomcat tomcat = provider.unwrap(Tomcat.class);

		assertEquals(8080, 
				Whitebox.getInternalState(tomcat, "port"));
	}
	
	@Test
	public void testOperation() throws Exception {
		provider.setConfiguration(new BasicConfiguration());
		Whitebox.setInternalState(provider, "await", false);
		provider.start();
		Thread.sleep(1000);
		provider.stop();
	}
	
	@Test
	public void testRunOK() throws Exception {
		BasicConfiguration config = new BasicConfiguration();
		config.setServerDir(Paths.get(".", "/target/tomcat"))
			.setHost("localhost").setPort(8085);

		Server server = Server.create(config);

		WebApp app = new WebApp();
		
		Path baseDir = Paths.get("target/test-classes");
		Files.createDirectories(baseDir);
		app.setSourceLocation(baseDir);
		app.setContextRoot("/test");	// TODO: handle slashes
		app.setWebBase(Paths.get("src/test/resources/web"));
		server.addApplication(app);
		
		Tomcat7Provider prov = server.unwrap(Tomcat7Provider.class);
		Whitebox.setInternalState(prov, "await", false);

		server.start();
		server.stop();
	}
}
