[![Maven Central](https://img.shields.io/maven-central/v/com.nordstrom.tools/settings.svg)](https://mvnrepository.com/artifact/com.nordstrom.tools/settings)

# SettingsCore

### Composition of configurations

The **SettingsCore** class extends [CompositeConfiguration](https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/CompositeConfiguration.html), using the facilities provided by this class to produce an aggregated configuration from three sources in the following order of precedence: 

1. System properties
2. (optional) Stored properties, typically from a properties file
3. (optional) Default values, typically specified in the enumeration

### Declaring stored properties

To specify stored properties for your configuration, override one of following methods:

* **`getStoredConfig`** - Your implementation returns a populated [Configuration](https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/Configuration.html) object.
* **`getInputStream`** - Your implementation returns an input stream supplying key/value pairs.
* **`getSettingsUrl`** - Your implementation returns the URL from which to load your settings.
* **`getSettingsPath`** - Your implementation returns the path from which to load your settings.

> **NOTE**: These methods are listed in order of evaluation, which stops at the first non-null response.  
> **NOTE**: Typical implementations override **`getSettingsPath`**, which will support most scenarios.  
> **NOTE**: Stored properties are declared in Apache's extended syntax. See [PropertiesConfiguration](https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/PropertiesConfiguration.html) for details.  
> **NOTE**: By overriding the **`getStoredConfig`** method, you're able to incorporate any arbitrary [Configuration](https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/Configuration.html) object you need into your settings - including another [CompositeConfiguration](https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/CompositeConfiguration.html) object.

### Specifying default values

Two methods have been provided for you to supply default values for your configuration:

* Specify default values as arguments of the constant declarations in your settings enumeration and override the **`SettingsAPI.val`** method. Specifying _'null'_ for a setting's default value indicates that no default exists.
* Alternatively, you can override the **`getDefaults`** method with your own implementation.

> **NOTE**: For settings collections with no default values, you can eliminate unnecessary processing in the core API by overriding **`getDefaults`** with a method that simply returns _'null'_.

It can be advantageous to create a hybrid of these two approaches - default values declared in the settings enumeration augmented by scenario-specific values produced by an override of the **`getDefaults`** method. You can see an example of this strategy in the [Selenium Foundation](https://github.com/sbabcoc/Selenium-Foundation) project.

The settings defined in this project include default values that are specific to the target **Selenium** API version:

* [Core Configuration](https://github.com/sbabcoc/Selenium-Foundation/blob/master/src/main/java/com/nordstrom/automation/selenium/AbstractSeleniumConfig.java#L498)
* [Selenium 2 Specific](https://github.com/sbabcoc/Selenium-Foundation/blob/master/src/selenium2/java/com/nordstrom/automation/selenium/SeleniumConfig.java#L207)
* [Selenium 3 Specific](https://github.com/sbabcoc/Selenium-Foundation/blob/master/src/selenium3/java/com/nordstrom/automation/selenium/SeleniumConfig.java#L219)

### Declaring configuration settings

Implementations of **SettingsCore** supply a context-specific enumeration (which extends **Enum&lt;T&gt;**) to provide the collection of settings needed in this context. This enumeration must implement the **SettingsAPI** interface to provide clients with a common method for retrieving configuration keys and to give the core settings implementation access to the constants and default values of the enumeration.

### Configuration examples

For example implementations of **SettingsCore**, check out this project's units tests. These demonstrate all of the major features of the API, including:

* Declaration of settings and default values
* Declaration and formatting of backing files
* Extraction of typed data
* Declaration of lists of values
* Overrides of stored and default values
* Configurable inclusion of sub-configurations
