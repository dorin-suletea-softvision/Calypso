package ui.forms;

import static sesion.props.UIPropSheet.DIAG_CMP_PAD_H;
import static sesion.props.UIPropSheet.DIAG_CMP_PAD_V;

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
import javax.swing.JTextField;

import sesion.Session;
import sesion.props.IOPropertySheet;
import sesion.props.Strings;
import sesion.props.UIPropSheet;
import ui.components.DialogHeader;
import ui.components.LineLabel;
import ui.controller.FacadeController;
import ui.resources.ResourceRetriever;

public class ExportSheetAsImageForm extends JDialog{
	private static final long serialVersionUID = 1L;
	private DialogHeader header;
	
	private JButton okBtn;
	private JButton cancelBtn;
	private JTextField imageNameTf;
	private JTextField exportFileTf;
	private JButton browseBtn;
	
	public ExportSheetAsImageForm(String sheetName) {
		super();
		setResizable(false);
		setSize(UIPropSheet.OPTIONS_DIALOG_FORM_DIM);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Sheet as Image");
		addComponents();
		initComponents(sheetName);
		addListeners();
	}
	
	private void addComponents(){
		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		// header
		header = new DialogHeader(DIAG_CMP_PAD_H, DIAG_CMP_PAD_V);
		header.initComponents("Image", "Export sheet as image", ResourceRetriever.loadImage(ResourceRetriever.EXPORT_ICON_IMG));
		getContentPane().add(header, BorderLayout.NORTH);
		
		
		// body
		JPanel bodyPan = new JPanel();
		bodyPan.setPreferredSize(new Dimension(UIPropSheet.OPTIONS_DIALOG_FORM_DIM.width, UIPropSheet.OPTIONS_DIALOG_FORM_DIM.height - UIPropSheet.DIAG_HEADER_FOOTER_DIM.height * 2));
		bodyPan.setLayout(null);
		getContentPane().add(bodyPan);

		JLabel sheetNameLbl = new JLabel("Image name");
		sheetNameLbl.setBounds(DIAG_CMP_PAD_V, DIAG_CMP_PAD_H, UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		bodyPan.add(sheetNameLbl);

		imageNameTf = new JTextField();
		imageNameTf.setBounds(sheetNameLbl.getX() + DIAG_CMP_PAD_V, sheetNameLbl.getY() + sheetNameLbl.getHeight(), UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		bodyPan.add(imageNameTf);

		JLabel sheetFileLbl = new JLabel("Save location");
		sheetFileLbl.setBounds(sheetNameLbl.getX(), imageNameTf.getY() + imageNameTf.getHeight() + DIAG_CMP_PAD_H, UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		bodyPan.add(sheetFileLbl);

		exportFileTf = new JTextField();
		exportFileTf.setBounds(sheetFileLbl.getX() + DIAG_CMP_PAD_V, sheetFileLbl.getY() + sheetFileLbl.getHeight(), UIPropSheet.DIAG_TF_DIM.width, UIPropSheet.DIAG_TF_DIM.height);
		exportFileTf.setEditable(false);
		bodyPan.add(exportFileTf);

		browseBtn = new JButton("Browse");
		browseBtn.setBounds(exportFileTf.getX() + (exportFileTf.getWidth() - UIPropSheet.BIG_BTN_DIM.width) / 2, exportFileTf.getY() + exportFileTf.getHeight() + DIAG_CMP_PAD_H / 2, UIPropSheet.BIG_BTN_DIM.width,
				UIPropSheet.BIG_BTN_DIM.height);
		bodyPan.add(browseBtn);

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

	
	private void addListeners(){
		imageNameTf.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String sheetFileText = exportFileTf.getText();
				int index = sheetFileText.lastIndexOf(IOPropertySheet.DIR_SEPARATOR);
				String toSave = sheetFileText.substring(0, index);
				exportFileTf.setText(toSave + "\\" + imageNameTf.getText() + IOPropertySheet.SHEET_IMAGE_EXTENSION);
			}
		});
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(new File(IOPropertySheet.DEFAUL_IMAGE_EXPORT_DIR));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int retval = fileChooser.showOpenDialog(ExportSheetAsImageForm.this);

				if (retval == JFileChooser.APPROVE_OPTION) {
					exportFileTf.setText(fileChooser.getSelectedFile().getAbsolutePath() + "\\" + imageNameTf.getText() + IOPropertySheet.SHEET_IMAGE_EXTENSION);
				}
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExportSheetAsImageForm.this.dispose();
			}
		});
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File sheetFile = new File(exportFileTf.getText());
				File sheetFileDir = new File(sheetFile.getParent());

				for (File f : sheetFileDir.listFiles())
					if (f.getAbsolutePath().equals(sheetFile.getAbsolutePath())) {
						if (JOptionPane.showConfirmDialog(ExportSheetAsImageForm.this, Strings.FILE_EXISTS_MESSAGE + exportFileTf.getText(), "Overwrite?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							// if overwrite
							FacadeController.getInstance().io_saveSheetAsImage(Session.getSelectedSheet(), sheetFile);
							ExportSheetAsImageForm.this.dispose();
						}
						return;
					}

				FacadeController.getInstance().io_saveSheetAsImage(Session.getSelectedSheet(), sheetFile);
				ExportSheetAsImageForm.this.dispose();
			}
		});
		
	}
	
	private void initComponents(String sheetName ){
		imageNameTf.setText(sheetName);
		header.setDescription("Export \""+ sheetName +"\" as image");
		exportFileTf.setText(IOPropertySheet.DEFAUL_IMAGE_EXPORT_DIR + "\\" + imageNameTf.getText() + IOPropertySheet.SHEET_IMAGE_EXTENSION);
	}
	


}
