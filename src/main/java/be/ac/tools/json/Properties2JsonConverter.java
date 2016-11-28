package be.ac.tools.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.google.gson.GsonBuilder;

/**
 * Converter for {@link java.util.Properties} to hierarchical JSON. <br>
 * Inspired by http://stackoverflow.com/questions/23871694/java-properties-to-json.
 *
 */
public class Properties2JsonConverter {

	private Properties properties;

	private boolean sanitizeValues;

	public Properties2JsonConverter(String propertiesFilePath, boolean sanitizeValues) throws IOException {
		loadProperties(propertiesFilePath);
		this.sanitizeValues = sanitizeValues;
	}

	public String toJson() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(toMap());
	}

	public File toJsonFile(String filePath) throws IOException {
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file);
		fw.write(toJson());
		fw.close();
		return file;
	}

	private void loadProperties(String propertiesFilePath) throws IOException {
		FileInputStream fis = new FileInputStream(new File(propertiesFilePath));
		properties = new Properties();
		properties.load(fis);
	}

	private Map<String, Object> toMap() {
		Map<String, Object> propertiesMap = new TreeMap<String, Object>();
		for (Object key : properties.keySet()) {
			// split key in parts based on . notation to build a tree
			List<String> keyParts = Arrays.asList(((String) key).split("\\."));
			// build the tree map for the key
			Map<String, Object> valueMap = buildTreeMap(keyParts, propertiesMap);
			// store value under the last key part
			String value = properties.getProperty((String) key);
			if (sanitizeValues) {
				value = value.trim();
			}
			valueMap.put(keyParts.get(keyParts.size() - 1), value);
		}
		return propertiesMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> buildTreeMap(List<String> keys, Map<String, Object> map) {
		// get map for the key or create it if it doesn't exist yet in the tree
		Map<String, Object> valueMap = (Map<String, Object>) map.get(keys.get(0));
		if (valueMap == null) {
			valueMap = new TreeMap<String, Object>();
			map.put(keys.get(0), valueMap);
		}
		// recursive call to do the same for the next keys
		if (keys.size() > 2) {
			// take next key and build up tree
			valueMap = buildTreeMap(keys.subList(1, keys.size()), valueMap);
		}
		// return the last valueMap in which we will store the actual value
		return valueMap;
	}

}
