package org.rss.sefac.test;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.glassfish.embeddable.GlassFish;
import org.junit.Test;
import org.rss.sefac.Server;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.BasicConfiguration;
import org.rss.sefac.spi.Glassfish3Provider;

public class GlassfishProviderTest {

	Glassfish3Provider provider = null;
	
	@Test(expected=IllegalStateException.class)
	public void testNoConfig() throws Exception {
		provider = new Glassfish3Provider();
		provider.addApplication(new WebApp());
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
		WebApp app = new WebApp();
		app.setContextRoot("test");
		app.setWebBase(Paths.get("src/test/resources"));
		app.setSourceLocation(Paths.get("target", "classes"));
		provider.setConfiguration(config);
		provider.addApplication(app);
	}
	
	@Test
	public void testFullConfig() throws Exception {
		BasicConfiguration config = new BasicConfiguration();
		config.setHost("localhost").setPort(3030)
			.setServerDir(Paths.get("target"));
		WebApp app = new WebApp();
		app.setContextRoot("test");
		app.setSourceLocation(Paths.get("target", "classes"));
		
		Server server = Server.create(config);
		server.addApplication(app);
		server.start();
		Thread.sleep(1500);
		server.stop();
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
	}
}
