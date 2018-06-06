package org.rss.sefac.test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.rss.sefac.Server;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.BasicConfiguration;
import org.rss.sefac.spi.Jetty9Provider;

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
		app.setContextRoot("/test");
		app.setWebApp(Paths.get("src/test/resources") /*baseDir*/);
		server.addApplication(app);
		
		Jetty9Provider prov = server.unwrap(Jetty9Provider.class);
//		Whitebox.setInternalState(prov, "await", false);

		server.start();
		server.stop();
	}
}
