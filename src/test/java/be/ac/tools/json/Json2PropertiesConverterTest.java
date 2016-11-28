package be.ac.tools.json;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Json2PropertiesConverterTest {

	private static final String PROPERTIES_FILE = "/test.properties";

	private static final String JSON_FILE = "/test.json";

	private String propertiesFilePath;

	private String jsonFilePath;

	@Before
	public void before() {
		propertiesFilePath = this.getClass().getResource(PROPERTIES_FILE).getFile();
		jsonFilePath = this.getClass().getResource(JSON_FILE).getFile();
	}

	@Test
	public void testJson2Properties() throws IOException {
		Properties properties = new Json2PropertiesConverter(jsonFilePath, true).toProperties();
		Assert.assertNotNull("Properties should not be null", properties);
		Assert.assertTrue("Properties should contain 7 items", properties.size() == 7);
	}

	@Test
	public void testJson2PropertiesFile() throws IOException {
		cleanup();
		File file = new Json2PropertiesConverter(jsonFilePath, true).toPropertiesFile(propertiesFilePath);
		Assert.assertTrue("File should exist", file.exists());
	}

	private void cleanup() {
		File file = new File(propertiesFilePath);
		if (file.exists()) {
			file.delete();
		}
	}

}
