package bridge;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import sesion.Session;
import sesion.props.BridgePropertySheet;
import ui.forms.MainForm;
import bridge.exceptions.BridgeException;
import bridge.exceptions.PathException;
import bridge.exceptions.PluginException;
import bridge.transferable.PluginInterface;


public class REFCLECT_TransferLayer extends TransferLayer{

	@Override
	public void bindPlugins() throws BridgeException {
		URLClassLoader loader = null;
		Class<?> lClass = null;
		URL urlPath=null;
		for (File f : getPlugins()) 
			try {
				urlPath=new URL("jar", "","file:" +f.getAbsolutePath()+"!/");
				loader = URLClassLoader.newInstance(new URL[]{urlPath});
				lClass = loader.loadClass(BridgePropertySheet.PLUGIN_IMPLEMENTATION_PATH);
				PluginInterface plugin = (PluginInterface) lClass.newInstance();
				plugin.setMainWindow(MainForm.getInstance());
				plugin.setSession(new Session());
				Session.addPlugin(plugin);
			}
		catch (MalformedURLException e){
			throw new PathException("Path to plugin "+f.getName()+" is invalid",e);
		}
		catch (SecurityException e){
			throw new PluginException("A security issue encontered with "+f.getName(),e);
		}
		catch (ClassNotFoundException e){
			throw new PluginException("Plugin "+f.getName()+" is broken",e);
		}
		catch (IllegalAccessException e){
			throw new PluginException("Plugin "+f.getName()+" is broken",e);
		}
		catch (IllegalArgumentException e){
			throw new PluginException("Plugin "+f.getName()+" is broken",e);
		}
		catch (InstantiationException e){
			throw new PluginException("Plugin "+f.getName()+" is broken",e);
		}
		
	}

}
