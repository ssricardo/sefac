package org.rss.sefac.spi;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;
import org.rss.sefac.app.Application;
import org.rss.sefac.app.WebApp;
import org.rss.sefac.config.Configuration;
import org.rss.sefac.exception.ConfigException;
import org.rss.sefac.exception.ExecutionException;

public class Glassfish3Provider implements ServerProvider {

	private static GlassFishRuntime runtime;
	private GlassFish glassfish;
	private Map<String, URI> appToDeploy = new HashMap<>();
	private boolean started = false;
	
	static {
		try {
			BootstrapProperties bp = new BootstrapProperties();
			runtime = GlassFishRuntime.bootstrap(bp);
		} catch (GlassFishException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public void setConfiguration(Configuration config) throws ConfigException {
		if (config == null) {
			throw new IllegalArgumentException();
		}

		try {
			GlassFishProperties glassfishProperties = new GlassFishProperties();
			if (config.getPort() != null) {
				glassfishProperties.setPort("http-listener", config.getPort());
			}
			
			if (config.getServerDir() != null) {
				Path p = config.getServerDir().toAbsolutePath();
				glassfishProperties.setProperty("org.glassfish.ejb.embedded.glassfish.installation.root", 
						p.toString());
			}

			glassfish = runtime.newGlassFish(glassfishProperties);
		} catch (GlassFishException e) {
			throw new ConfigException(e);
		}
	}

	@Override
	public void addApplication(Application app) throws ConfigException {
		if (glassfish == null) {
			throw new IllegalStateException("Server instance isn't configured yet. "
					+ "A former call to setConfiguration() is required.");
		}
		try {
			if (Files.isRegularFile(app.getSourceLocation())) {
				appToDeploy.put(app.getName(), app.getSourceLocation().toUri());
			} else {
				ScatteredArchive archive = null;
				if (app instanceof WebApp) {
					setupWebApp(app, archive);
				} else {
					archive = new ScatteredArchive(app.getName(), ScatteredArchive.Type.JAR);
					appToDeploy.put(app.getName(), archive.toURI());
				}
			}
			
			deployIfReady();
		} catch (GlassFishException | IOException e) {
			throw new ConfigException(e);
		}
	}

	private void setupWebApp(Application app, ScatteredArchive archive) throws IOException {
		WebApp wApp = (WebApp) app;
		
		if (wApp.getWebApp() != null) {
			archive = new ScatteredArchive(wApp.getName(), 
					ScatteredArchive.Type.WAR, wApp.getWebApp().toFile());
		}

		if (wApp.getSourceLocation() != null) {
			archive.addClassPath(app.getSourceLocation().toFile());
		}
		
		appToDeploy.put(wApp.getContextRoot(), archive.toURI());
	}

	private void deployIfReady()
			throws GlassFishException, IOException {
		
		if (started) {
			Deployer deployer = glassfish.getDeployer();
			for (Entry<String, URI> entry : appToDeploy.entrySet()) {
				deployer.deploy(entry.getValue(), "--contextroot=" + entry.getKey());
			}
			appToDeploy.clear();
		}
	}

	@Override
	public void start() throws ExecutionException {
		try {
			glassfish.start();
			started = true;
			deployIfReady();
		} catch (GlassFishException | IOException e) {
			throw new ExecutionException(e);
		}
	}

	@Override
	public void stop() throws ExecutionException {
		try {
			glassfish.stop();
			started = false;
		} catch (GlassFishException e) {
			throw new ExecutionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> klass) {
		if (ServerProvider.class.equals(klass)) {
			return (T) this;
		} else if (GlassFish.class.equals(klass)) {
			return (T) this.glassfish;
		}
		return null;
	}

	@Override
	public void setDeveloperMode(boolean value) {
		throw new IllegalStateException("Not implemented yet");
	}

}
