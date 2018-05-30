package org.rss.sefac.spi;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;
import org.rss.sefac.app.Application;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.Configuration;
import org.rss.sefac.exception.ConfigException;
import org.rss.sefac.exception.ExecutionException;
import org.rss.sefac.spi.ServerProvider;

public class Tomcat7Provider implements ServerProvider {

	private static final String TMP = System.getProperty("java.io.tmpdir");
	private static final Integer DEFAULT_PORT = 8080;
	private Tomcat tomcat;
	private boolean await;

	public Tomcat7Provider() {
		this.tomcat = new Tomcat(); 
	}
	
	@Override
	public void setConfiguration(Configuration config) {
		if (config == null) {
			throw new IllegalArgumentException("Configuration must not be null");
		}
		tomcat.setBaseDir((config.getServerDir() != null) 
				? config.getServerDir().toString() : TMP);
		tomcat.setPort((config.getPort() != null) ? config.getPort() : DEFAULT_PORT);
//		tomcat.setHost(new HostConfig().);
		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);
		await = true;
	}
	
	@Override
	public void addApplication(Application app) throws ConfigException {
		if (! (app instanceof WebApp)) {
			throw new ConfigException("Tomcat supports only Web Applications. "
					+ "You should use a WebApp instance in its addApplication");
		}
		WebApp wApp = (WebApp) app;
		try {
			StandardContext ctx = (StandardContext) 
					tomcat.addWebapp(wApp.getContextRoot(), 
							wApp.getWebBase().toAbsolutePath().toString());

			processAppResources(app, ctx);
		} catch (ServletException e) {
		}
	}

	private void processAppResources(Application app, StandardContext ctx) {
		VirtualDirContext resources = new VirtualDirContext();
		String resPath = "/WEB-INF/classes=" 
				+ app.getSourceLocation().toAbsolutePath().toString();
		resources.setExtraResourcePaths(resPath);
		ctx.setResources(resources);
	}

	@Override
	public void start() throws ExecutionException {
		try {
			this.tomcat.start();
			if (await) {
				this.tomcat.getServer().await();
			}
		} catch (LifecycleException e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public void stop() throws ExecutionException {
		try {
			this.tomcat.stop();
		} catch (LifecycleException e) {
			throw new ExecutionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> klass) {
		if (Tomcat.class.equals(klass)) {
			return (T) tomcat;
		}
		if (Tomcat7Provider.class.equals(klass)) {
			return (T) this;
		}
		return null;
	}

	@Override
	public void setDeveloperMode(boolean value) {
		// TODO Auto-generated method stub
		
	}

}
