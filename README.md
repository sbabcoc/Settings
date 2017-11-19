[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.nordstrom.test-automation.tools/settings/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nordstrom.test-automation.tools/settings)

# SettingsCore

### Composition of configurations

The **SettingsCore** class extends **CompositeConfiguration**, using the facilities provided by this class to produce an aggregated configuration from three sources in the following order of precedence: 

1. System properties
2. (optional) Stored properties, typically from a properties file
3. (optional) Default values, typically specified in the enumeration

### Declaring stored properties

To specify stored properties for your configuration, override one of following methods:

* **`getStoredConfig`** - Your implementation returns a populated **Configuration** object.
* **`getInputStream`** - Your implementation returns an input stream supplying key/value pairs.
* **`getSettingsUrl`** - Your implementation returns the URL from which to load your settings.
* **`getSettingsPath`** - Your implementation returns the path from which to load your settings.

> **NOTE**: These methods are listed in order of evaluation, stopping at the first non-null response.  
> **NOTE**: Typical implementations override **`getSettingsPath`**, which will support most scenarios.  
> **NOTE**: Stored properties are declared in Apache's extended syntax. See **PropertiesConfiguration** for details.  
> **NOTE**: By overriding the **`getStoredConfig`** method, you're able to incorporate any arbitrary **Configuration** object you need into your settings - including another **CompositeConfiguration** object.

### Specifying default values

Two methods have been provided for you to supply default values for your configuration:

* Specify default values as arguments of the constant declarations in your settings enumeration and override the **`SettingsAPI.val`** method. Specifying _'null'_ for a setting's default value indicates that no default exists.
* Alternatively, you can override the **`getDefaults`** method with your own implementation.

> **NOTE**: For settings collections with no default values, you can eliminate unnecessary processing in the core API by overriding **`getDefaults`** with a method that simply returns _'null'_.

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