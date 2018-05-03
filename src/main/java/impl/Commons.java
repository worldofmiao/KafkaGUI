package impl;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;

/**
 * Commons class for some universal methods
 *
 */

public class Commons {
	/**
	 * Logger Instance
	 */
	private static final Logger LOGGER = Logger.getLogger(Commons.class);

	/**
	 * Wait method
	 */
	public static void waitFor(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			LOGGER.debug(e.getMessage());
		}
	}

	/**
	 * Method that get file content from resources folder to String,need to set
	 * project compiler to JAVA 1.7
	 * 
	 * @param fileName
	 *            : file name in resources folder
	 * @return String: JSON body as a string
	 */
	public String getFile(String fileName) {
		StringBuilder result = new StringBuilder("");
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return result.toString();
	}

	/**
	 * Method that load setting properties
	 */
	public Map<String, String> loadSettings() {
		Map<String, String> settings = new HashMap<String, String>();
		ClassLoader classLoader = getClass().getClassLoader();
		// use ini4j to support multi-environment configs
		String env = "settings";
		Ini ini = new Ini();
		try {
			File settingFile = new File("setting.txt");
			if (!settingFile.exists()) {
				String brokerURL = "";
				String topic = "";
				String partition = "";

				LOGGER.debug(brokerURL);
				LOGGER.debug(topic);
				LOGGER.debug(partition);
				settings.put("brokerURL", brokerURL);
				settings.put("topic", topic);
				settings.put("partition", partition);
			} else {
				ini.load(new File("setting.txt"));
				Section section = ini.get(env);
				String brokerURL = section.get("broker");
				String topic = section.get("topic");
				String partition = section.get("partition");

				LOGGER.debug(brokerURL);
				LOGGER.debug(topic);
				LOGGER.debug(partition);
				settings.put("brokerURL", brokerURL);
				settings.put("topic", topic);
				settings.put("partition", partition);

			}
		} catch (InvalidFileFormatException e) {
			LOGGER.error(e.toString());
		} catch (IOException e) {
			LOGGER.error(e.toString());
		}
		return settings;
	}

	public void saveSettings(Map<String, String> map) {
		ClassLoader classLoader = getClass().getClassLoader();
		// use ini4j to support multi-environment configs
		// String env = "settings";
		// Ini ini = new Ini();
		try {
			File settingFile = new File("setting.txt");
			if (!settingFile.exists()) {
				settingFile.createNewFile();
			}
			Wini ini = new Wini(settingFile);
			// ini.load(classLoader.getResourceAsStream("setting.txt"));
			// Section section = ini.get(env);
			String brokerURL = map.get("brokerURL");
			String topic = map.get("topic");
			String partition = map.get("partition");

			LOGGER.debug(brokerURL);
			LOGGER.debug(topic);
			LOGGER.debug(partition);
			ini.put("settings", "broker", brokerURL);
			ini.put("settings", "topic", topic);
			ini.put("settings", "partition", partition);
			ini.store();

		} catch (InvalidFileFormatException e) {
			LOGGER.error(e.toString());
		} catch (IOException e) {
			LOGGER.error(e.toString());
		}
	}

	public boolean testURL(String url) {
		boolean OK = false;
		Properties properties = new Properties();
		properties.put("bootstrap.servers", url);
		properties.put("connections.max.idle.ms", 1000);
		properties.put("request.timeout.ms", 1000);
		try (AdminClient client = KafkaAdminClient.create(properties)) {
			ListTopicsResult topics = client.listTopics();
			Set<String> names = topics.names().get();
			if (names.isEmpty()) {
				// case: if no topic found.
			}
			OK = true;
			return OK;
		} catch (InterruptedException | ExecutionException e) {
			OK = false;
			return OK;
		}
	}

}
