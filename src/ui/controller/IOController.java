package ui.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import sesion.Session;
import sesion.props.Strings;
import ui.forms.MainForm;
import ui.resources.ResourceRetriever;
import ui.resources.ioexceptions.BadFilePathException;
import ui.resources.ioexceptions.SaveLoadIOException;
import bridge.exceptions.ComlianceException;
import engine.model.SheetModel;
import engine.views.SheetView;

public class IOController {

	public SheetView createNewSheet(String sheetName, File sheetFile, long pluginSignature) {
		SheetView sheet = new SheetView(pluginSignature);
		sheet.setName(sheetName);
		sheet.getModel().setDiskFile(sheetFile);
		return sheet;
	}

	public void saveSheet(SheetModel sheet) {
		try {
			sheet.setCommited(true);
			ResourceRetriever.saveSheet(sheet, sheet.getDiskFile().getAbsolutePath());
		} catch (SaveLoadIOException e) {
			e.printStackTrace();
		}
		System.out.println("Current sheet saved successful at : " + sheet.getDiskFile().getAbsolutePath());
	}

	public void saveSheetAsImage(SheetView sheet, File file) {
		BufferedImage image = new BufferedImage(sheet.getWidth(), sheet.getHeight(), BufferedImage.TYPE_INT_ARGB);
		sheet.paintAll(image.getGraphics());
		sheet.paint(image.getGraphics());
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Current sheet exported as img successful at : " + file);
	}

	public void openSheet(String absolutePath) {
		try {
			SheetModel model = ResourceRetriever.loadSheet(absolutePath);
			// this is added here to grant the possibility to import sheets form
			// another directory structure , since the model of sheet contains path to the 
			//phisical file , when file structure changes the model has to be updated
			if (absolutePath != model.getDiskFile().getAbsolutePath())
				model.setDiskFile(new File(absolutePath));
			// !!

			SheetView svh = new SheetView(model);

			if (MainForm.getInstance().getSheetTabPane().isSheetInEditor(svh)) {
				JOptionPane.showMessageDialog(MainForm.getInstance(), Strings.FILE_IN_EDITOR_MESSAGE);
				Session.setSelectedSheet(svh);
				return;
			}
			else
				Session.setSelectedSheet(svh);

			svh.initComponents();
		} catch (BadFilePathException e) {
			JOptionPane.showMessageDialog(MainForm.getInstance(), e.getMessage());
		} catch (SaveLoadIOException e) {
			e.printStackTrace();
		} catch (ComlianceException e) {
			e.printStackTrace();
		}
	}
	

}
