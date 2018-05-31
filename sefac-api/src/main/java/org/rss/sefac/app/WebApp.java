package org.rss.sefac.app;

import java.nio.file.Path;

/**
 * Representation of an WebApp, adding attributes to base {@link Application}
 * @author Ricardo SS
 *
 */
public class WebApp extends Application {

	public WebApp(String appName) {
		super(appName);
	}

	private String context;
	
	private Path webApp;

	public void setContextRoot(String ctx) {
		this.context = ctx;
	}

	public String getContextRoot() {
		return context;
	}

	public Path getWebApp() {
		return webApp;
	}

	public void setWebApp(Path webBase) {
		this.webApp = webBase;
	}
	
}
