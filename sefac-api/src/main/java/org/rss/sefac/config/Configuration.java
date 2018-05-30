package org.rss.sefac.config;

import java.nio.file.Path;

/**
 * Represents and holds Configuration of a Server
 * @author Ricardo Saturnino
 */
public interface Configuration {

	/**
	 * @see #getServerDir()
	 * @return this instance
	 */
	Configuration setServerDir(Path path);
	
	/**
	 * @see #getPort()
	 * @return this instance
	 */
	Configuration setPort(Integer port);
	
	/**
	 * @see #getHost()
	 * @return this instance
	 */
	Configuration setHost(String host);

	/**
	 * Represents the location where the server you install its resources and run.
	 * It may be a fix directory, a relative path or a temp one
	 * @return
	 */
	Path getServerDir();
	
	/**
	 * Represents the port which the server runs
	 * @return
	 */
	Integer getPort();
	
	/**
	 * Used to point to a customized host
	 * @return
	 */
	String getHost();
}
