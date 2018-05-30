package org.rss.sefac.config;

import java.nio.file.Path;

/**
 * Configuration implementation for simple programmatically use.
 * @see Configuration
 * @author Ricardo SS
 */
public class BasicConfiguration implements Configuration {

	private Path baseDir;
	private Integer port;
	private String host;

	@Override
	public BasicConfiguration setServerDir(Path path) {
		this.baseDir = path;
		return this;
	}

	@Override
	public BasicConfiguration setPort(Integer port) {
		this.port = port;
		return this;
	}

	@Override
	public BasicConfiguration setHost(String host) {
		this.host = host;
		return this;
	}

	public Path getServerDir() {
		return baseDir;
	}

	public Integer getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}
	
}
