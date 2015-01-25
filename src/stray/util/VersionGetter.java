package stray.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import stray.Main;
import stray.Settings;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;


public class VersionGetter {

	/**
	 * NOTE: This method blocks until it fails or completes
	 */
	public static void getVersionFromServer(){
		final String path = "https://raw.githubusercontent.com/chrislo27/Stray-core/master/version.txt";
		long start = System.currentTimeMillis();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new URL(path)
					.openStream()));

			StringBuilder file = new StringBuilder();
			String inputline;
			while ((inputline = br.readLine()) != null)
				file.append(inputline);

			br.close();
			
			Main.logger.info("Finished getting version, took "
					+ (System.currentTimeMillis() - start) + " ms");
			
			JsonValue value = new JsonReader().parse(file.toString());

			Main.latestVersionNumber = value.getInt("version_number", 0);
			Main.latestVersion = value.getString("version", "");

			if (Settings.debug) Main.logger
					.debug("JSON obtained from host: " + file.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Main.latestVersionNumber = -1;
		} catch (IOException e) {
			e.printStackTrace();
			Main.latestVersionNumber = -1;
		} catch (NullPointerException e) {
			Main.logger.error("Failed to parse/get latest version info", e);
			Main.latestVersionNumber = -1;
		}
	}
}
