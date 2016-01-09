package bridge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bridge.exceptions.BridgeException;
import bridge.exceptions.PathException;
import sesion.props.BridgePropertySheet;

public abstract class TransferLayer {
	public abstract void bindPlugins()throws BridgeException;
	
	protected List<File> getPlugins() throws BridgeException {
		List<File> ret = new ArrayList<File>();
		File repo = new File(BridgePropertySheet.PLUGIN_REPO);
		if (!repo.isDirectory())
			throw new PathException(repo + " is not a directory");
		File plugins[] = repo.listFiles();
		Pattern pattern = Pattern.compile(BridgePropertySheet.PLUGIN_FILENAME_SIGNATURE);

		for (File f : plugins) {
			Matcher matcher = pattern.matcher(f.getName());
			if (matcher.find())
				ret.add(f);
		}
		return ret;
	}
}
