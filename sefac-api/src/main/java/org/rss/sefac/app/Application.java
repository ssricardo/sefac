package org.rss.sefac.app;

import java.nio.file.Path;

/**
 * Base representation of a deployable to the server  
 * @author Ricardo SS
 */
public abstract class Application {

	private Path sourceLocation;
	
	public Path getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(Path baseDir) {
		this.sourceLocation = baseDir;
	}

}
