package stray.util.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import stray.Main;
import stray.Settings;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.zafarkhaja.semver.Version;

public class VersionGetter {

	private static VersionGetter instance;

	private VersionGetter() {
	}

	public static VersionGetter instance() {
		if (instance == null) {
			instance = new VersionGetter();
			instance.loadResources();
		}
		return instance;
	}

	private VersionDiff difference = VersionDiff.CHECKING;

	private void loadResources() {

	}

	public static VersionDiff getDiff() {
		return instance().difference;
	}

	/**
	 * NOTE: This method blocks until it fails or completes
	 */
	public void getVersionFromServer() {
		final String path = "https://raw.githubusercontent.com/chrislo27/Stray-core/master/version.txt";
		long start = System.currentTimeMillis();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new URL(path).openStream()));

			StringBuilder file = new StringBuilder();
			String inputline;
			while ((inputline = br.readLine()) != null)
				file.append(inputline);

			br.close();

			Main.logger.info("Finished getting version, took "
					+ (System.currentTimeMillis() - start) + " ms");

			JsonValue value = new JsonReader().parse(file.toString());

			Main.latestVersion = value.getString("version", "");
			
			Version current = Version.valueOf(Main.version.replace("v", ""));
			Version server = Version.valueOf(Main.latestVersion.replace("v", ""));

			int diff = current.compareTo(server);
			
			if(diff == 0){
				difference = VersionDiff.EQUAL;
			}else if(diff < 0){
				difference = VersionDiff.OUTDATED;
			}else if(diff > 0){
				difference = VersionDiff.FUTURE;
			}else difference = VersionDiff.INVALID;

			if (Settings.debug) Main.logger.debug("JSON obtained from host: " + file.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			difference = VersionDiff.INVALID;
		} catch (IOException e) {
			e.printStackTrace();
			difference = VersionDiff.INVALID;
		} catch (NullPointerException e) {
			Main.logger.error("Failed to parse/get latest version info", e);
			difference = VersionDiff.INVALID;
		}
	}
}
