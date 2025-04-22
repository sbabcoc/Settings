package com.nordstrom.automation.settings;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * This class ensures that we don't get bitten again by the re-appearance of Commons Configuration
 * bug <a href='https://issues.apache.org/jira/browse/CONFIGURATION-851'>CONFIGURATION-851</a>.
 */
public class HomeFolderTest {

    private static final String STRING_STORED = "STORED";
    private static final int INTEGER_DEFAULT = 3;
    
    @Rule
    public TemporaryFileRule tempFileRule = new TemporaryFileRule();

    @Test
    public void testUsingTempFile() throws ConfigurationException, IOException {
        Path tempFile = tempFileRule.getTempFile();
        String stringSetting = HomeSettings.STRING_STORED.key() + "=" + STRING_STORED + "\n";
        Files.write(tempFile, stringSetting.getBytes(StandardCharsets.UTF_8));
        
        SettingsCore<HomeSettings> config = new SettingsCore<HomeSettings>(HomeSettings.class) {
            @Override
            public String getSettingsPath() {
                return tempFile.getFileName().toString();
            }
        };
        
        assertEquals("Incorrect stored string value", STRING_STORED, config.getString(HomeSettings.STRING_STORED.key()));
        assertEquals("Incorrect default integer value", INTEGER_DEFAULT, config.getInt(HomeSettings.INTEGER_STORED.key()));
    }
    
    public enum HomeSettings implements SettingsCore.SettingsAPI {
        STRING_STORED("home.string.stored", "DEFAULT"),
        INTEGER_STORED("home.integer.stored", "3");
        
        private String propertyName;
        private String defaultValue;
        
        HomeSettings(String propertyName, String defaultValue) {
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
    

}
