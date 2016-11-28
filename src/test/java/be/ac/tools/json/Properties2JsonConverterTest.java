package be.ac.tools.json;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Properties2JsonConverterTest {

	private static final String PROPERTIES_FILE = "/test.properties";

	private static final String JSON_FILE = "/test.json";

	private String propertiesPath;

	private String jsonPath;

	@Before
	public void before() {
		propertiesPath = this.getClass().getResource(PROPERTIES_FILE).getFile();
		jsonPath = this.getClass().getResource(JSON_FILE).getFile();
	}

	@Test
	public void testProperties2Json() throws IOException {
		String json = new Properties2JsonConverter(propertiesPath, true).toJson();
		Assert.assertTrue("JSON should not be empty", StringUtils.isNotEmpty(json));
	}

	@Test
	public void testProperties2JsonFile() throws IOException {
		cleanup();
		File file = new Properties2JsonConverter(propertiesPath, true).toJsonFile(jsonPath);
		Assert.assertTrue("File should exist", file.exists());
	}

	private void cleanup() {
		File file = new File(jsonPath);
		if (file.exists()) {
			file.delete();
		}
	}

}
