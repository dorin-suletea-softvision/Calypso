package ui.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import ui.resources.ioexceptions.BadFilePathException;
import ui.resources.ioexceptions.BrokeFileException;
import ui.resources.ioexceptions.SaveLoadIOException;
import engine.model.SheetModel;

public class ResourceRetriever {
	public static final String APPLOGO_BIG = "img/app_logo_big.png";

	public static final String DEFAULT_CONNECTOR_CONTEXT_BTN_IMG = "img/default_connector_context_btn_img.png";
	public static final String SHEET_ICON_IMG = "img/sheet_icon.png";
	public static final String EXPORT_ICON_IMG = "img/export_icon.png";
	public static final String PLUGIN_ICON_IMG = "img/plugin_icon.png";

	public static final String NEW_ICON_IMG = "img/new_btn_img.png";
	public static final String OPEN_ICON_IMG = "img/open_btn_img.png";
	public static final String EXPORT_AS_IMAGE_ICON_IMG = "img/export_as_image_btn_icon.png";
	public static final String SAVE_ICON_IMG = "img/save_btn_img.png";
	public static final String SAVE_ALL_ICON_IMG = "img/save_all_btn_img.png";
	public static final String UNDO_ICON_IMG = "img/undo_btn_img.png";
	public static final String REDO_ICON_IMG = "img/redo_btn_img.png";
	public static final String DELETE_ICON_IMG = "img/delete_btn_img.png";

	public static final String ALIGN_TOP_ICON_IMG = "img/align_top_btn_img.png";
	public static final String ALIGN_BOT_ICON_IMG = "img/align_bot_btn_img.png";
	public static final String ALIGN_LEFT_ICON_IMG = "img/align_left_btn_img.png";
	public static final String ALIGN_RIGHT_ICON_IMG = "img/align_right_btn_img.png";
	public static final String ALIGN_VERTICAL_SPACE_ICON_IMG = "img/align_space_vertical_btn_img.png";
	public static final String ALIGN_HORIZONTAL_SPACE_ICON_IMG = "img/align_space_horizontal_btn_img.png";

	public static ImageIcon loadImage(String imgPath) {
		return new ImageIcon(ResourceRetriever.class.getResource(imgPath));
	}

	public static SheetModel loadSheet(String path) throws SaveLoadIOException {
		SheetModel ret = null;
		File f = new File(path);
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ret = (SheetModel) ois.readObject();
			ois.close();
			return ret;
		} catch (FileNotFoundException e) {
			throw new BadFilePathException(path);
		} catch (IOException e) {
			throw new BrokeFileException(f);
		} catch (ClassNotFoundException e) {
			throw new BrokeFileException(f);
		}
	}

	public static void saveSheet(SheetModel sheet, String path) throws SaveLoadIOException {
		File f = new File(path);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			ObjectOutputStream ois = new ObjectOutputStream(fos);
			ois.writeObject(sheet);
			ois.flush();
			ois.close();
		} catch (FileNotFoundException e) {
			throw new BadFilePathException(path);
		} catch (IOException e) {
			throw new BrokeFileException(f, e);
		}

	}
}
