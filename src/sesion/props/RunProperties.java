package sesion.props;

import java.io.File;
import java.net.URISyntaxException;

public class RunProperties {
	public static boolean persistentLayout = false;
	public static final boolean RUN_TEST_MODE = false;

	public static final String HOME = RUN_TEST_MODE ? "E:\\Dropbox\\Calypso_Home" : getJarLocation();

	/**
	 * The main config file has to be created by the installer in the folder
	 * where the jar and the bat/sh runner of the application from this file
	 * will be initialized all property sheet parameters that depend on the user
	 * machine configurations.
	 */
	private static String MAIN_CFG_FILE_NAME = "config.txt";

	private static String getJarLocation() {
		try {
			return new File(RunProperties.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("Unexpected Exception: " + e);
		}
		return "";
	}
}
