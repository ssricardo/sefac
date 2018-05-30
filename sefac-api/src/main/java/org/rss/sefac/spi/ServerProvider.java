package org.rss.sefac.spi;

import org.rss.sefac.app.Application;
import org.rss.sefac.config.Configuration;
import org.rss.sefac.exception.ConfigException;
import org.rss.sefac.exception.ExecutionException;

/**
 * Contract and adapter to real server implementations.
 * Use internal or for new/custom supported servers.
 * @author Ricardo SS
 */
public interface ServerProvider {
	
	void setConfiguration(Configuration config) throws ConfigException;
	
	void addApplication (Application app) throws ConfigException;
	
	void start() throws ExecutionException;
	
	void stop() throws ExecutionException;
	
	<T> T unwrap(Class<T> klass);
	
	void setDeveloperMode(boolean value);
}
