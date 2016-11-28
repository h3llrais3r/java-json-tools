package be.ac.tools.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import com.google.gson.Gson;

/**
 * Converter for JSON to {@link java.util.Properties}. <br>
 * Inspired by http://stackoverflow.com/questions/54295/how-to-write-java-util-properties-to-xml-with-sorted-keys.
 */
public class Json2PropertiesConverter {

	private String jsonFilePath;

	private boolean sanitizeValues;

	public Json2PropertiesConverter(String jsonFilePath, boolean sanitizeValues) throws IOException {
		this.jsonFilePath = jsonFilePath;
		this.sanitizeValues = sanitizeValues;
	}

	public Properties toProperties() throws IOException {
		return buildProperties(toMap());
	}

	public File toPropertiesFile(String propertiesFilePath) throws IOException {
		// write properties to a file
		File file = new File(propertiesFilePath);
		FileWriter writer = new FileWriter(file);
		toProperties().store(writer, null);
		writer.close();
		return file;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> toMap() throws IOException {
		FileReader reader = new FileReader(jsonFilePath);
		Gson gson = new Gson();
		Map<String, Object> propertiesMap = gson.fromJson(reader, Map.class);
		reader.close();
		return propertiesMap;
	}

	@SuppressWarnings("serial")
	private Properties buildProperties(Map<String, Object> propertiesMap) {
		// override to have sorted properties
		// http://stackoverflow.com/questions/17011108/how-can-i-write-java-properties-in-a-defined-order
		Properties properties = new Properties() {
			@Override
			public Enumeration<Object> keys() {
				return Collections.enumeration(new TreeSet<Object>(super.keySet()));
			}
		};
		for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
			String key = entry.getKey();
			// build each property
			buildProperty(key, entry.getValue(), properties);
		}
		return properties;
	}

	@SuppressWarnings("unchecked")
	private void buildProperty(String key, Object value, Properties properties) {
		// value is a map -> need to go further to build the key of the property
		if (value instanceof Map) {
			for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
				// recursive call to build the complete key of the property
				buildProperty(key + "." + entry.getKey(), entry.getValue(), properties);
			}
		} else {
			// value is not longer a map but a string -> property value
			if (sanitizeValues) {
				value = ((String) value).trim();
			}
			properties.put(key, value);
		}
	}

}
