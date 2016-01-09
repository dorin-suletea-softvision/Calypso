package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import sesion.Session;
import sesion.props.RunProperties;
import srcp.laf.LookAndFeelFactory;
import srcp.laf.LookAndFeelNames;
import ui.controller.FacadeController;
import ui.forms.MainForm;
import bridge.REFCLECT_TransferLayer;
import bridge.TransferLayer;
import bridge.exceptions.BridgeException;
import bridge.transferable.PluginInterface;

public class Main {

	public static void main(String[] args) throws BridgeException {
		// setting the LAF

		LookAndFeelFactory.setLaF(LookAndFeelNames.NIMROD);

		// Importing the plugins
		TransferLayer a = new REFCLECT_TransferLayer();
		a.bindPlugins();

		// Show UI
		MainForm.getInstance().setVisible(true);
		MainForm.getInstance().addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (FacadeController.getInstance().menu_exit() == 0) {
					MainForm.getInstance().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

		// TESTS
		for (PluginInterface i : Session.getPlugins())
			System.out.println(i.getPluginName() + " " + i.getAuthor() + " " + i.getReleaseDate());

		System.out.println("Home Folder" + RunProperties.HOME);
		System.out.println("Runing in test mode " + RunProperties.RUN_TEST_MODE);
	}
}
