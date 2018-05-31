package org.rss.sefac.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ServiceLoader;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.rss.sefac.Server;
import org.rss.sefac.app.JarApplication;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.BasicConfiguration;
import org.rss.sefac.spi.ServerProvider;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value=ServiceLoader.class)
public class BasicConfigTest {

	@Mock
    private static ServiceLoader<ServerProvider> mockServiceLoader;
	
	// FIXME: mock not working
	@Ignore()
	@Test(expected=IllegalStateException.class)
	public void testNoProvider() throws Exception {
		PowerMockito.mockStatic(ServiceLoader.class);
		when(ServiceLoader.load(ServerProvider.class)).thenReturn(mockServiceLoader);
		Server.create(new BasicConfiguration());
	}
	
	@Test
	public void testApi() throws Exception {
		System.setProperty("org.apache.jasper.level", "DEBUG");
		
		BasicConfiguration config = new BasicConfiguration();
		config.setServerDir(Paths.get(".", "/target/tomcat"))
			.setHost("localhost").setPort(8080);

		Server server = Server.create(config);

		WebApp app = new WebApp("testApi");
		
		Path baseDir = Paths.get("target/test-classes");
				//Files.createTempDirectory("sefac").toAbsolutePath()/* Paths.get(System.getenv("java.io.tmp"))*/;
		Files.createDirectories(baseDir);
		app.setSourceLocation(baseDir);
		app.setContextRoot("/test");	// TODO: handle slashes
		app.setWebApp(Paths.get("src/test/resources/web"));
		server.addApplication(app);
		
		server.start();
		server.stop();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoName() throws Exception {
		new JarApplication(null);
	}
	
	@Test
	public void testProviderOK() throws Exception {
//		Files.createTempDirectory(Paths.get("META-INF/services"), "");
		/*Path basePath = Paths.get(getClass().getResource("/basic.config.test.properties").toURI());
		Path runtimeDir = basePath.resolve("..");
	    		
		Files.createSymbolicLink( 
				runtimeDir.resolve(Paths.get("META-INF", "services", ServerProvider.class.getName())), 
				basePath);
		System.setProperty(ServerProvider.class.getName(), BasicTestProvider.class.getName());*/
		Server.create(new BasicConfiguration());
	}

}
