package org.rss.sefac.app;

import java.nio.file.Path;

public class WebApp extends Application {

	private String context;
	
	private Path webBase;

	public void setContextRoot(String ctx) {
		this.context = ctx;
	}

	public String getContextRoot() {
		return context;
	}

	public Path getWebBase() {
		return webBase;
	}

	public void setWebBase(Path webBase) {
		this.webBase = webBase;
	}
	
}
