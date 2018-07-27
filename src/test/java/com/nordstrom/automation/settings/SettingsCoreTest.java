package com.nordstrom.automation.settings;

import static org.testng.Assert.assertEquals;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;
import com.nordstrom.automation.settings.NarfConfig.NarfSettings;
import com.nordstrom.automation.settings.SetsConfig.SetsSettings;

public class SettingsCoreTest {
	
	private static final String STRING_SYSTEM = "SYSTEM";
	private static final String STRING_STORED = "STORED";
	private static final String STRING_DEFAULT = "DEFAULT";
	private static final int INTEGER_SYSTEM = 1;
	private static final int INTEGER_STORED = 2;
	private static final int INTEGER_DEFAULT = 3;
	private static final String[] STRING_ARRAY = {"ONE", "TWO", "THREE"};
	
	private static final String SET1 = "set1.properties";
	private static final String SET2 = "set2.properties";
	private static final String FIRST_ONE = "first one";
	private static final String FIRST_TWO = "first two";
	
	@Test
	public void testBasics() throws ConfigurationException, IOException {
		System.setProperty(NarfSettings.STRING_SYSTEM.key(), STRING_SYSTEM);
		System.setProperty(NarfSettings.INTEGER_SYSTEM.key(), Integer.toString(INTEGER_SYSTEM));
		
		NarfConfig config = new NarfConfig();
		
		assertEquals(config.getString(NarfSettings.STRING_SYSTEM.key()), STRING_SYSTEM, "Incorrect system string value");
		assertEquals(config.getString(NarfSettings.STRING_STORED.key()), STRING_STORED, "Incorrect stored string value");
		assertEquals(config.getString(NarfSettings.STRING_DEFAULT.key()), STRING_DEFAULT, "Incorrect default string value");
		assertEquals(config.getInt(NarfSettings.INTEGER_SYSTEM.key()), INTEGER_SYSTEM, "Incorrect system integer value");
		assertEquals(config.getInt(NarfSettings.INTEGER_STORED.key()), INTEGER_STORED, "Incorrect stored integer value");
		assertEquals(config.getInt(NarfSettings.INTEGER_DEFAULT.key()), INTEGER_DEFAULT, "Incorrect default integer value");
		assertEquals(config.getStringArray(NarfSettings.MULTIPLE_ENTRY.key()), STRING_ARRAY, "Incorrect string array value");
	}
	
	@Test
	public void testSet1() throws ConfigurationException, IOException {
		System.setProperty(SetsSettings.INCLUDE_NAME.key(), SET1);
		
		SetsConfig config = new SetsConfig();
		
		assertEquals(config.getString(SetsSettings.INCLUDE_NAME.key()), SET1, "Incorrect 'include' name");
		assertEquals(config.getString(SetsSettings.FIRST_KEY.key()), FIRST_ONE, "Incorrect included value");
	}
	
	@Test
	public void testSet2() throws ConfigurationException, IOException {
		System.setProperty(SetsSettings.INCLUDE_NAME.key(), SET2);
		
		SetsConfig config = new SetsConfig();
		
		assertEquals(config.getString(SetsSettings.INCLUDE_NAME.key()), SET2, "Incorrect 'include' name");
		assertEquals(config.getString(SetsSettings.FIRST_KEY.key()), FIRST_TWO, "Incorrect included value");
	}
}
