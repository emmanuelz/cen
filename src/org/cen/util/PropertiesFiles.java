package org.cen.util;

import java.util.Properties;

public class PropertiesFiles {
	private static Properties gameboardProperties;

	public static Properties getGameboardProperties() {
		if (gameboardProperties == null) {
			synchronized (PropertiesFiles.class) {
				if (gameboardProperties == null) {
					gameboardProperties = new Properties();
				}
			}
		}
		return gameboardProperties;
	}
}
