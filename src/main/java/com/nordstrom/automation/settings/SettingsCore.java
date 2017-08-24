package com.nordstrom.automation.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

/**
 * This class extends {@link CompositeConfiguration}, using the facilities provided by this class to
 * produce an aggregated configuration from three sources in the following order of precedence:
 * 
 * <ol>
 *     <li>System properties</li>
 *     <li>(optional) Stored properties, typically from a <i>properties</i> file</li>
 *     <li>(optional) Default values, typically specified in the enumeration</li>
 * </ol>
 * 
 * To specify stored properties for your configuration, override one of following methods:<br>
 * <br>
 * <ul>
 *         <li>{@link #getStoredConfig} - Your implementation returns a populated {@link Configuration} object.</li>
 *         <li>{@link #getInputStream} - Your implementation returns an input stream supplying key/value pairs.</li>
 *         <li>{@link #getSettingsUrl} - Your implementation returns the URL from which to load your settings.</li>
 *         <li>{@link #getSettingsPath} - Your implementation returns the path from which to load your settings.</li>
 * </ul>
 * 
 * <b>NOTE</b>: These methods are listed in order of evaluation, stopping at the first non-null response.<br>
 * <b>NOTE</b>: Typical implementations override {@link #getSettingsPath}, which will support most scenarios.<br>
 * <b>NOTE</b>: Stored properties are declared in Apache's extended syntax. See {@link PropertiesConfiguration} for details.<br>
 * <b>NOTE</b>: By overriding the {@link #getStoredConfig} method, you're able to incorporate any arbitrary 
 * {@link Configuration} object you need into your settings - including another {@link CompositeConfiguration} object.<br>
 * <br>
 * Two methods have been provided for you to supply default values for your configuration:<br>
 * <br> 
 * <ul>
 *         <li>Specify default values as arguments of the constant declarations in your settings enumeration 
 *             and override the {@link SettingsAPI#val} method. Specifying 'null' for a setting's default value 
 *             indicates that no default exists.</li>
 *        <li>Alternatively, you can override the {@link #getDefaults} method with your own implementation.</li>
 *  </ul>
 * 
 * <b>NOTE</b>: For settings collections with no default values, you can eliminate unnecessary processing in 
 * the core API by overriding {@link #getDefaults} with a method that simply returns 'null'.
 *
 * @param <T>
 *         Implementations of {@code SettingsCore} supply a context-specific enumeration (which extends 
 *        {@code Enum<T>}) to provide the collection of settings needed in this context. This
 *        enumeration must implement the {@link SettingsAPI} interface to provide clients with a common
 *        method for retrieving configuration keys and to give the core settings implementation access 
 *        to the constants and default values of the enumeration.
 */
public class SettingsCore<T extends Enum<T> & SettingsCore.SettingsAPI> extends CompositeConfiguration {
    
    public static final String PROPS_FILE = "propsFile";
    
    private final Class<T> enumClass;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private SystemConfiguration system = new SystemConfiguration();
    private Configuration properties;
    private MapConfiguration defaults;
    
    public SettingsCore(Class<T> enumClass) throws ConfigurationException, IOException {
        // save enumeration class
        this.enumClass = enumClass;
        // add system properties
        addConfiguration(system);
        
        // get stored configuration
        properties = getStoredConfig();
        
        // if properties still undefined
        if (properties == null) {
            // get configuration input stream
            InputStream inputStream = getInputStream();
            // if input stream provided
            if (inputStream != null) {
                // get properties from input stream
                properties = new PropertiesConfiguration();
                ((PropertiesConfiguration) properties).read(new InputStreamReader(inputStream));
            }
        }
        
        // if properties still undefined
        if (properties == null) {
            // get settings URL
            URL url = getSettingsUrl();
            // if setting URL provided
            if (url != null) {
                Configurations configs;
                try {
                    // get properties from URL
                    configs = new Configurations();
                    properties = configs.properties(url);
                } catch (ConfigurationException e) {
                    propagateIfNotMissingFile(e);
                    logger.warn("Unable to locate configuration at URL '{}'", url);
                }
            }
        }
        
        // if properties still undefined
        if (properties == null) {
            // get settings file path
            String path = getSettingsPath();
            // if settings file path provided
            if (path != null) {
                Configurations configs;
                try {
                    // get properties from file path
                    configs = new Configurations();
                    properties = configs.properties(path);
                } catch (ConfigurationException e) {
                    propagateIfNotMissingFile(e);
                    logger.warn("Unable to locate configuration at path '{}'", path);
                }
            }
        }
        
        // if properties defined
        if (properties != null) {
            // add defined properties
            addConfiguration(properties);
        }
        
        // get enumeration default values
        Map<String, String> defaultsMap = getDefaults();
        // if populated default values collection provided
        if ( ! ((defaultsMap == null) || (defaultsMap.isEmpty()))) {
            // add enumeration default values
            defaults = new MapConfiguration(defaultsMap);
            addConfiguration(defaults);
        }
    }
    
    /**
     * Get stored property declarations as a configuration object.
     * 
     * @return populated {@link Configuration} object (may be 'null')
     * @see #getSettingsPath
     * @see #getSettingsUrl
     * @see #getInputStream
     */
    public Configuration getStoredConfig() {
        return null;
    }
    
    /**
     * Get stored property declarations as an input stream.<br>
     * <b>NOTE</b>: Property values are typically stored in <i>property</i> files.
     * 
     * @return property list (key and element pairs) as an input stream (may be 'null')
     * @see #getSettingsPath
     * @see #getSettingsUrl
     * @see #getStoredConfig
     */
    public InputStream getInputStream() {
        return null;
    }
    
    /**
     * Get the URL for a stored property declarations file.
     * 
     * @return property file URL (may be 'null')
     * @see #getSettingsPath
     * @see #getInputStream
     * @see #getStoredConfig
     */
    public URL getSettingsUrl() {
        return null;
    }
    
    /**
     * Get the path to a stored property declarations file.<br>
     * <b>NOTE</b>: The returned path can be absolute, relative, or a simple filename. See
     * {@link org.apache.commons.configuration2.io.FileLocatorUtils#DEFAULT_LOCATION_STRATEGY 
     * DEFAULT_LOCATION_STRATEGY} for details of the strategy employed by the underlying 
     * file-based configuration API to locate the specified file.
     * 
     * @return property file path (may be 'null')
     * @see #getSettingsUrl
     * @see #getInputStream
     * @see #getStoredConfig
     */
    public String getSettingsPath() {
        return null;
    }

    /**
     * Get defined system property default values<br>
     * <b>NOTE</b>: Default values are optional. Entries for properties without default values should be omitted.
     * 
     * @return defined system property default values (may be 'null')
     */
    protected Map<String, String> getDefaults() {
        Map<String, String> defaults = new HashMap<>();
        for (SettingsAPI setting : enumClass.getEnumConstants()) {
            if (setting.val() != null) {
                defaults.put(setting.key(), setting.val());
            }
        }
        return defaults;
    }
    
    /**
     * Propagate the specified configuration exception if it wasn't caused by a missing file.
     * 
     * @param thrown configuration exception to be evaluated
     */
    protected void propagateIfNotMissingFile(ConfigurationException thrown) {
        String message = thrown.getMessage();
        if ((message != null) && (message.startsWith("Could not locate"))) {
            return;
        }
        throw Throwables.propagate(thrown);
    }
    
    /**
     * If a properties file is specified via a System property named {@code propsFile} or the [propsFile] argument, the 
     * settings in this file are injected into the System properties collection. Note that existing System properties 
     * override property file settings.<br>
     * <br>
     * <b>NOTE</b>: The strategy employed to locate the specified file is defined by 
     * {@link org.apache.commons.configuration2.io.FileLocatorUtils#DEFAULT_LOCATION_STRATEGY DEFAULT_LOCATION_STRATEGY}
     * 
     * @param propsFile properties file name (may be 'null')
     */
    public static void injectProperties(String propsFile) {
        String path = System.getProperty(PROPS_FILE);
        if (path == null) path = propsFile;
        
        if (path != null) {
            try {
                Configurations configs = new Configurations();
                PropertiesConfiguration properties = configs.properties(path);
                Iterator<String> i = properties.getKeys();
                while (i.hasNext()) {
                    String propName = i.next();
                    if (System.getProperty(propName) == null) {
                        System.setProperty(propName, properties.getString(propName));
                    }
                }
            } catch (ConfigurationException e) {
                LoggerFactory.getLogger(SettingsCore.class).warn("Failure encountered injecting properties from path '{}'", path);
            }
        }
    }
    
    /**
     * This interface defines the methods of enumerations declaring configuration settings that are
     * used by the core settings implementation. The {@link #key} method must be implemented by the
     * enumeration, but the {@link #val} method is provided with a default implementation. If your
     * enumeration specifies default values, override {@link #val} with a method that returns these
     * values.
     */
    public interface SettingsAPI {
        
        /**
         * Get the key for this configuration setting, which is its system property name
         * 
         * @return configuration setting key
         */
        String key();
        
        /**
         * Get the default value for this configuration setting.<br>
         * <b>NOTE</b>: Return 'null' if this setting has no default value.
         * 
         * @return configuration setting default value; 'null' if none exists
         */
        default String val() {
            return null;
        }
    }
    
}
