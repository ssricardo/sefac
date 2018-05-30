package org.rss.sefac;

import java.util.ServiceLoader;

import org.rss.sefac.app.Application;
import org.rss.sefac.config.Configuration;
import org.rss.sefac.exception.ConfigException;
import org.rss.sefac.exception.ExecutionException;
import org.rss.sefac.spi.ServerProvider;

public class Server {

	private ServerProvider provider;
	
	private Server() {
		loadProvider();
	}

	public static Server create(Configuration config) throws ConfigException {
		Server s = new Server();
		s.provider.setConfiguration(config);
		return s;
	}
	
	private void loadProvider() {
		ServiceLoader<ServerProvider> candidates = ServiceLoader.load(ServerProvider.class);
		if (candidates == null || ! candidates.iterator().hasNext()) {
			throw new IllegalStateException("No Server Provider were found! "
					+ "You need to either add a library containing it to the runtime classpath "
					+ "or create your own and register it.");
		}
		provider = candidates.iterator().next();
	}
	
	public Server addApplication (Application app) throws ConfigException {
		provider.addApplication(app);
		return this;
	}
	
	public void start() throws ExecutionException {
		provider.start();
	}
	
	public void stop() throws ExecutionException {
		provider.stop();
	}
	
	public <T> T unwrap(Class<T> klass) {
		return provider.unwrap(klass);
	}
	
	public Server setDeveloperMode(boolean value) {
		provider.setDeveloperMode(value);
		return this;
	}
}
