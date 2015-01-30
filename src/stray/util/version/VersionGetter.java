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

			difference = compareVersions(Main.version, Main.latestVersion);

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

	public static VersionDiff compareVersions(String current, String server) {
		int i = compareVersionsInt(current, server);
		if (i == 0) {
			return VersionDiff.EQUAL;
		} else if (i == 1) {
			return VersionDiff.NEWER;
		} else if (i == -1) {
			return VersionDiff.OLDER;
		} else return VersionDiff.INVALID;
	}

	/**
	 * uses integers to determine version differences 0 is same, positive is the
	 * build version is newer than server, negative is the build version is
	 * older than server
	 * 
	 * @param current
	 * @param server
	 * @return
	 */
	public static int compareVersionsInt(String current, String server) {
		String str1 = current;
		String str2 = server;

		// bleh way of handling alpha and beta
		if (str1.endsWith("-alpha")) {
			str1 = str1.replace("-alpha", ".1");
		} else if (str1.endsWith("-beta")) {
			str1 = str1.replace("-beta", ".2");
		}
		if (str2.endsWith("-alpha")) {
			str2 = str2.replace("-alpha", ".1");
		} else if (str2.endsWith("-beta")) {
			str2 = str2.replace("-beta", ".2");
		}

		String[] vals1 = str1.split("\\.");
		String[] vals2 = str2.split("\\.");
		int i = 0;
		// set index to first non-equal ordinal or length of shortest version
		// string
		while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
			i++;
		}

		try {
			// compare first non-equal ordinal number
			if (i < vals1.length && i < vals2.length) {
				int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
				return Integer.signum(diff);
			}
			// the strings are equal or one string is a substring of the other
			// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
			else {
				return Integer.signum(vals1.length - vals2.length);
			}
		} catch (NumberFormatException e) {
			Main.logger.error(
					"Failed to parse versions \"" + current + "\" and \"" + server + "\"", e);
			return 404;
		}
	}
}
