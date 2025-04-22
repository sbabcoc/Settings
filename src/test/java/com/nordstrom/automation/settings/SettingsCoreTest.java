package com.nordstrom.automation.settings;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

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
        
        assertEquals("Incorrect system string value", STRING_SYSTEM, config.getString(NarfSettings.STRING_SYSTEM.key()));
        assertEquals("Incorrect stored string value", STRING_STORED, config.getString(NarfSettings.STRING_STORED.key()));
        assertEquals("Incorrect default string value", STRING_DEFAULT, config.getString(NarfSettings.STRING_DEFAULT.key()));
        assertEquals("Incorrect system integer value", INTEGER_SYSTEM, config.getInt(NarfSettings.INTEGER_SYSTEM.key()));
        assertEquals("Incorrect stored integer value", INTEGER_STORED, config.getInt(NarfSettings.INTEGER_STORED.key()));
        assertEquals("Incorrect default integer value", INTEGER_DEFAULT, config.getInt(NarfSettings.INTEGER_DEFAULT.key()));
        assertArrayEquals("Incorrect string array value", STRING_ARRAY, config.getStringArray(NarfSettings.MULTIPLE_ENTRY.key()));
    }
    
    @Test
    public void testSet1() throws ConfigurationException, IOException {
        System.setProperty(SetsSettings.INCLUDE_NAME.key(), SET1);
        
        SetsConfig config = new SetsConfig();
        
        assertEquals("Incorrect 'include' name", SET1, config.getString(SetsSettings.INCLUDE_NAME.key()));
        assertEquals("Incorrect included value", FIRST_ONE, config.getString(SetsSettings.FIRST_KEY.key()));
    }
    
    @Test
    public void testSet2() throws ConfigurationException, IOException {
        System.setProperty(SetsSettings.INCLUDE_NAME.key(), SET2);
        
        SetsConfig config = new SetsConfig();
        
        assertEquals("Incorrect 'include' name", SET2, config.getString(SetsSettings.INCLUDE_NAME.key()));
        assertEquals("Incorrect included value", FIRST_TWO, config.getString(SetsSettings.FIRST_KEY.key()));
    }
}
