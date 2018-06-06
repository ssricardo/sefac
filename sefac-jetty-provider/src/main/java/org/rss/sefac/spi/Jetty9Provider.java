package org.rss.sefac.spi;

import java.util.Arrays;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.rss.sefac.app.Application;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.Configuration;
import org.rss.sefac.exception.ConfigException;
import org.rss.sefac.exception.ExecutionException;

public class Jetty9Provider implements ServerProvider {

	private Server server;

	@Override
	public void setConfiguration(Configuration config) throws ConfigException {
		System.setProperty("jetty.home", config.getServerDir().toAbsolutePath().toString());
        System.setProperty("jetty.base", config.getServerDir().toAbsolutePath().toString());
        
		server = new Server();

		ServerConnector serverConn = new ServerConnector(server);
		serverConn.setPort(config.getPort());
//		serverConn.setHost(config.getHost());
		server.addConnector(serverConn);
	}

	@Override
	public void addApplication(Application app) throws ConfigException {
		
		if (app instanceof WebApp) {
			WebApp wApp = (WebApp) app;
			
			/*ContextHandler context = new ContextHandler("/");
			server.setHandler(context);*/
			
			/*ResourceHandler resourceHandler = new ResourceHandler();
			resourceHandler.setDirectoriesListed(true);
			resourceHandler.setWelcomeFiles(new String[] { "index.html" });
			resourceHandler.setResourceBase(wApp.getWebApp().toString());
			context.setHandler(resourceHandler);*/
			
			WebAppContext webapp = new WebAppContext();
			webapp.setContextPath(wApp.getContextRoot());
			webapp.setResourceBase(wApp.getWebApp().toString());
			/*webapp.setConfigurations(new Configuration[] 
			        { 
			            new AnnotationConfiguration(),
			            new WebInfConfiguration(), 
			            new WebXmlConfiguration(),
			            new MetaInfConfiguration(), 
			            new FragmentConfiguration(), 
			            new EnvConfiguration(),
			            new PlusConfiguration(), 
			            new JettyWebXmlConfiguration() 
			        });*/
			webapp.setConfigurations(new org.eclipse.jetty.webapp.Configuration[] {
	                new AnnotationConfiguration(), new WebXmlConfiguration(),
	                new WebInfConfiguration(),
	                new PlusConfiguration(), new MetaInfConfiguration(),
	                new FragmentConfiguration(), new EnvConfiguration() });
			
			
	        webapp.setParentLoaderPriority(true);
			webapp.getMetaData().setWebInfClassesDirs(
					Arrays.asList(
							Resource.newResource(app.getSourceLocation().toAbsolutePath().toFile())));
//			webapp.setServer(server);
			server.setHandler(webapp);
			
//			context.setTempDirectory(new File("/some/dir/foo"));
//			context.setAttribute("org.eclipse.jetty.webapp.basetempdir", "/tmp/foo");
		}

	}

	@Override
	public void start() throws ExecutionException {
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public void stop() throws ExecutionException {
		try {
			server.stop();
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> klass) {
		if (ServerProvider.class.equals(klass)) {
			return (T) this;
		} else if (Server.class.equals(klass)) {
			return (T) server;
		}
		return null;
	}

	@Override
	public void setDeveloperMode(boolean value) {
		// TODO Auto-generated method stub

	}

}
