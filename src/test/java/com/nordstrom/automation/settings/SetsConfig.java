package com.nordstrom.automation.settings;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;

public class SetsConfig extends SettingsCore<SetsConfig.SetsSettings> {

	private static final String SETTINGS_FILE = "base.properties";

	public SetsConfig() throws ConfigurationException, IOException {
		super(SetsSettings.class);
	}

	public enum SetsSettings implements SettingsCore.SettingsAPI {
		INCLUDE_NAME("include.name"),
		FIRST_KEY("first.key"),
		SECOND_KEY("second.key"),
		THIRD_KEY("third.key");
		
		private String propertyName;
		
		SetsSettings(String propertyName) {
			this.propertyName = propertyName;
		}
		
		@Override
		public String key() {
			return propertyName;
		}
	}
	
	@Override
	public String getSettingsPath() {
		return SETTINGS_FILE;
	}

	@Override
	public Map<String, Object> getDefaults() {
		return null;
	}
}
