package ui.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sesion.Session;
import sesion.props.IOPropertySheet;
import sesion.props.Strings;
import sesion.props.UIPropSheet;
import ui.components.DialogHeader;
import ui.components.LineLabel;
import ui.components.PluginsListItem;
import ui.components.RichList;
import ui.controller.FacadeController;
import ui.resources.ResourceRetriever;
import bridge.exceptions.ComlianceException;
import bridge.transferable.PluginInterface;
import engine.views.SheetView;
import static sesion.props.UIPropSheet.*;

public class CreateSheetForm extends JDialog {
	private static final long serialVersionUID = 1L;
	private static CreateSheetForm instance = new CreateSheetForm();
	
	private DialogHeader header;
	private JButton okBtn;
	private JButton cancelBtn;
	private JTextField sheetNameTf;
	private JTextField sheetFileTf;
	private JButton browseBtn;
	private RichList pluginJList;
	
	public static CreateSheetForm getInstace(){
		instance.sheetNameTf.setText("New Sheet");
		instance.sheetFileTf.setText(IOPropertySheet.DEFAUL_WORKSPACE_DIR + "\\" + instance.sheetNameTf.getText() + IOPropertySheet.SHEET_EXTENSION);
		return instance;
	}
	
	private CreateSheetForm() {
		setResizable(false);
		setSize(UIPropSheet.OPTIONS_DIALOG_FORM_DIM);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Create Sheet");
		addComponents();
		initComponents();
		addListeners();
	}

	private void addComponents() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		// header
		header = new DialogHeader(DIAG_CMP_PAD_H, DIAG_CMP_PAD_V);
		header.initComponents("Sheet", "Create a new stand-alone sheet", ResourceRetriever.loadImage(ResourceRetriever.SHEET_ICON_IMG));
		getContentPane().add(header, BorderLayout.NORTH);

		// body
		JPanel bodyPan = new JPanel();
		bodyPan.setPreferredSize(new Dimension(UIPropSheet.OPTIONS_DIALOG_FORM_DIM.width, UIPropSheet.OPTIONS_DIALOG_FORM_DIM.height - UIPropSheet.DIAG_HEADER_FOOTER_DIM.height * 2));
		bodyPan.setLayout(null);
		getContentPane().add(bodyPan);

		JLabel sheetNameLbl = new JLabel("Sheet name");
		sheetNameLbl.setBounds(DIAG_CMP_PAD_V, DIAG_CMP_PAD_H, UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		bodyPan.add(sheetNameLbl);

		sheetNameTf = new JTextField();
		sheetNameTf.setBounds(sheetNameLbl.getX() + DIAG_CMP_PAD_V, sheetNameLbl.getY() + sheetNameLbl.getHeight(), UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		bodyPan.add(sheetNameTf);

		JLabel sheetFileLbl = new JLabel("Save location");
		sheetFileLbl.setBounds(sheetNameLbl.getX(), sheetNameTf.getY() + sheetNameTf.getHeight() + DIAG_CMP_PAD_H, UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		bodyPan.add(sheetFileLbl);

		sheetFileTf = new JTextField();
		sheetFileTf.setBounds(sheetFileLbl.getX() + DIAG_CMP_PAD_V, sheetFileLbl.getY() + sheetFileLbl.getHeight(), UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		sheetFileTf.setEditable(false);
		bodyPan.add(sheetFileTf);

		browseBtn = new JButton("Browse");
		browseBtn.setBounds(sheetFileTf.getX() + (sheetFileTf.getWidth() - UIPropSheet.BIG_BTN_DIM.width) / 2, sheetFileTf.getY() + sheetFileTf.getHeight() + DIAG_CMP_PAD_H / 2,
				UIPropSheet.BIG_BTN_DIM.width, UIPropSheet.BIG_BTN_DIM.height);
		bodyPan.add(browseBtn);

		pluginJList = new RichList();
		JScrollPane pluginJListSp = new JScrollPane(pluginJList);
		pluginJListSp.setBounds(UIPropSheet.OPTIONS_DIALOG_FORM_DIM.width - UIPropSheet.RICH_LIST_DIM.width - DIAG_CMP_PAD_V * 2, sheetNameTf.getY(), UIPropSheet.RICH_LIST_DIM.width,
				bodyPan.getPreferredSize().height - sheetNameTf.getY() - DIAG_CMP_PAD_H * 2 - UIPropSheet.SCROLL_BAR_W);
		bodyPan.add(pluginJListSp);

		JLabel pluginJListLbl = new JLabel("Associated Plugin");
		pluginJListLbl.setBounds(pluginJListSp.getX() - DIAG_CMP_PAD_V, sheetNameLbl.getY(), UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		bodyPan.add(pluginJListLbl);

		// footer
		JPanel btnPannel = new JPanel();
		btnPannel.setLayout(null);
		btnPannel.setSize(UIPropSheet.DIAG_HEADER_FOOTER_DIM);
		btnPannel.setPreferredSize(UIPropSheet.DIAG_HEADER_FOOTER_DIM);
		getContentPane().add(btnPannel, BorderLayout.SOUTH);

		LineLabel lineLbl = new LineLabel(UIPropSheet.OPTIONS_DIALOG_FORM_DIM.width - DIAG_CMP_PAD_V * 2);
		lineLbl.setLocation(DIAG_CMP_PAD_V, 0);
		btnPannel.add(lineLbl);

		okBtn = new JButton("Ok");
		okBtn.setSize(UIPropSheet.BIG_BTN_DIM);
		okBtn.setLocation(DIAG_CMP_PAD_V, (btnPannel.getHeight() - okBtn.getHeight()) / 2);
		btnPannel.add(okBtn);

		cancelBtn = new JButton("Cancel");
		cancelBtn.setSize(UIPropSheet.BIG_BTN_DIM);
		cancelBtn.setLocation(okBtn.getX() + okBtn.getWidth() + DIAG_CMP_PAD_V, (btnPannel.getHeight() - cancelBtn.getHeight()) / 2);
		btnPannel.add(cancelBtn);
	}

	private void addListeners() {
		sheetNameTf.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String sheetFileText = sheetFileTf.getText();
				int index = sheetFileText.lastIndexOf(IOPropertySheet.DIR_SEPARATOR);
				String toSave = sheetFileText.substring(0, index);
				sheetFileTf.setText(toSave + "\\" + sheetNameTf.getText() + IOPropertySheet.SHEET_EXTENSION);
			}
		});

		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(new File(IOPropertySheet.DEFAUL_WORKSPACE_DIR));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int retval = fileChooser.showOpenDialog(CreateSheetForm.this);

				if (retval == JFileChooser.APPROVE_OPTION) {
					sheetFileTf.setText(fileChooser.getSelectedFile().getAbsolutePath() + "\\" + sheetNameTf.getText() + IOPropertySheet.SHEET_EXTENSION);
				}
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreateSheetForm.this.dispose();
			}
		});

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File sheetFile = new File(sheetFileTf.getText());
				File sheetFileDir = new File(sheetFile.getParent());

				for (File f : sheetFileDir.listFiles())
					if (f.getAbsolutePath().equals(sheetFile.getAbsolutePath())) {
						if (JOptionPane.showConfirmDialog(CreateSheetForm.this, Strings.FILE_EXISTS_MESSAGE + sheetFileTf.getText(), "Overwrite?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							// if overwrite
							SheetView shv = FacadeController.getInstance().io_createNewSheet(sheetNameTf.getText(), new File(sheetFileTf.getText()),
									((PluginsListItem) pluginJList.getSelectedValue()).getPlugin().getSignature());

							if (MainForm.getInstance().getSheetTabPane().getTabIndexBySheet(shv) != -1)// the
																											// sheet
																											// is
																											// open
																											// in
																											// editor
																											// ,
																											// remove
																											// it
								MainForm.getInstance().getSheetTabPane().removeTabAt(MainForm.getInstance().getSheetTabPane().getTabIndexBySheet(shv));

							FacadeController.getInstance().io_saveSheet(shv.getModel());
							try {
								Session.setSelectedSheet(shv);
							} catch (ComlianceException e1) {
								e1.printStackTrace();
							}
							CreateSheetForm.this.dispose();
						}
						return;
					}

				SheetView shv = FacadeController.getInstance().io_createNewSheet(sheetNameTf.getText(), new File(sheetFileTf.getText()),
						((PluginsListItem) pluginJList.getSelectedValue()).getPlugin().getSignature());

				FacadeController.getInstance().io_saveSheet(shv.getModel());
				try {
					Session.setSelectedSheet(shv);
				} catch (ComlianceException e1) {
					e1.printStackTrace();
				}
				CreateSheetForm.this.dispose();
			}
		});
	}

	private void initComponents() {
		sheetNameTf.setText("New Sheet");

		sheetFileTf.setText(IOPropertySheet.DEFAUL_WORKSPACE_DIR + "\\" + sheetNameTf.getText() + IOPropertySheet.SHEET_EXTENSION);

		for (PluginInterface plugin : Session.getPlugins()) {
			pluginJList.addComponent(new PluginsListItem(ResourceRetriever.loadImage(ResourceRetriever.PLUGIN_ICON_IMG), plugin));
		}
		pluginJList.setSelectedIndex(0);
	}

}
