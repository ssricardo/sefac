package org.rss.sefac;

import java.util.ServiceLoader;

import org.rss.sefac.app.Application;
import org.rss.sefac.config.Configuration;
import org.rss.sefac.exception.ConfigException;
import org.rss.sefac.exception.ExecutionException;
import org.rss.sefac.spi.ServerProvider;

/**
 * Main point in the API.
 * Retrieves a real server available and stabilish communication with it
 * @author Ricardo SS
 */
public class Server {

	private ServerProvider provider;
	
	private Server() {
		loadProvider();
	}

	/**
	 * Get Server instance based on supplied configuration 
	 * @param config required
	 * @return Server instance
	 * @throws ConfigException
	 */
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
	
	/**
	 * @param app
	 * @return
	 * @throws ConfigException
	 */
	public Server addApplication (Application app) throws ConfigException {
		provider.addApplication(app);
		return this;
	}
	
	/**
	 * @throws ExecutionException
	 */
	public void start() throws ExecutionException {
		provider.start();
	}
	
	/**
	 * @throws ExecutionException
	 */
	public void stop() throws ExecutionException {
		provider.stop();
	}
	
	/**
	 * Way to get a specific instance from the provider attached in runtime
	 * Useful when trying to use APIs not yet supported here 
	 * @param class: Type to be retrieved; ex.: Tomcat.class
	 * @return
	 * 	The instance request if it is supported by the provider;
	 *  otherwise null
	 */
	public <T> T unwrap(Class<T> klass) {
		return provider.unwrap(klass);
	}
	
	/**
	 * Provides functions specific to development phase
	 * @param value
	 * @return
	 */
	public Server setDeveloperMode(boolean value) {
		provider.setDeveloperMode(value);
		return this;
	}
}
