package stray.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import stray.Main;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;


public class VersionGetter {

	/**
	 * NOTE: This method blocks until it fails or completes
	 */
	public static void getVersionFromServer(){
		final String path = "https://raw.githubusercontent.com/chrislo27/Stray-core/master/version.txt";
		Main.startVersionCheck = System.currentTimeMillis();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new URL(path)
					.openConnection().getInputStream()));

			StringBuilder file = new StringBuilder();
			String inputline;
			while ((inputline = br.readLine()) != null)
				file.append(inputline);

			br.close();

			JsonValue value = new JsonReader().parse(file.toString());

			Main.latestVersionNumber = value.getInt("version_number", 0);
			Main.latestVersion = value.getString("version", "");

			Main.logger.info("Finished getting version, took "
					+ (System.currentTimeMillis() - Main.startVersionCheck) + " ms");
			if (Main.debug) Main.logger
					.debug("JSON obtained from host: " + file.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			Main.logger.error("Failed to parse/get latest version info", e);
		}
	}
}
