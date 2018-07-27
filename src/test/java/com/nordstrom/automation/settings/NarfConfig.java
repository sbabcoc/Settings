package com.nordstrom.automation.settings;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

public class NarfConfig extends SettingsCore<NarfConfig.NarfSettings> {
	
	private static final String SETTINGS_FILE = "narf.properties";

	public NarfConfig() throws ConfigurationException, IOException {
		super(NarfSettings.class);
	}

	public enum NarfSettings implements SettingsCore.SettingsAPI {
		STRING_DEFAULT("narf.string.default", "DEFAULT"),
		STRING_SYSTEM("narf.string.system", "DEFAULT"),
		STRING_STORED("narf.string.stored", "DEFAULT"),
		INTEGER_DEFAULT("narf.integer.property", "3"),
		INTEGER_SYSTEM("narf.integer.system", "3"),
		INTEGER_STORED("narf.integer.stored", "3"),
		MULTIPLE_ENTRY("narf.list.multiple", null),
		NONE_SPECIFIED("narf.none.specified", null);
		
		private String propertyName;
		private String defaultValue;
		
		NarfSettings(String propertyName, String defaultValue) {
			this.propertyName = propertyName;
			this.defaultValue = defaultValue;
		}
		
		@Override
		public String key() {
			return propertyName;
		}

		@Override
		public String val() {
			return defaultValue;
		}
	}
	
	@Override
	public String getSettingsPath() {
		return SETTINGS_FILE;
	}
}
