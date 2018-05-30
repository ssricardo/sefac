package org.rss.sefac.test.provider;

import org.rss.sefac.app.Application;
import org.rss.sefac.config.Configuration;
import org.rss.sefac.exception.ConfigException;
import org.rss.sefac.exception.ExecutionException;
import org.rss.sefac.spi.ServerProvider;

public class BasicTestProvider implements ServerProvider {

	@Override
	public void setConfiguration(Configuration config) throws ConfigException {
		System.out.println("setConfiguration");
		System.out.println(config);
	}

	@Override
	public void addApplication(Application app) throws ConfigException {
		System.out.println("addApplication");
		System.out.println(app);
	}

	@Override
	public void start() throws ExecutionException {
		System.out.println("start()");
	}

	@Override
	public void stop() throws ExecutionException {
		System.out.println("stop()");
	}

	@Override
	public <T> T unwrap(Class<T> klass) {
		System.out.println("unwrap: " + klass);
		return null;
	}

	@Override
	public void setDeveloperMode(boolean value) {
		System.out.println("developerMode");
	}
	
}