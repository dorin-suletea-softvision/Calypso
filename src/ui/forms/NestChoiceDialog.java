package ui.forms;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import sesion.props.Strings;
import ui.controller.FacadeController;
import engine.views.Glyph;

public class NestChoiceDialog extends JDialog{
	private static final long	serialVersionUID	= 1L;
	private JPanel 				contentPane;
	
	public NestChoiceDialog() {
		setModal(true);
		contentPane=new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		getContentPane().add(new JScrollPane(contentPane));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		

	}
	
	private void centerDialog(){
		pack();
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenDim.width/2-getWidth()/2,screenDim.height/2-getHeight()/2);
	}

	public void  initComponents(Hashtable<Glyph, List<Glyph>> intersectionTable) {
		JPanel messageLblPan = new JPanel();
		messageLblPan.setLayout(new BoxLayout(messageLblPan, BoxLayout.X_AXIS));
		JLabel messageLbl = new JLabel(Strings.nestChoiceDiagMSG);
		messageLbl.setBorder(BorderFactory.createTitledBorder(Strings.infoMSG));
		messageLblPan.add(messageLbl);
		
		
		JPanel sharedGroupPan =  new JPanel();
		sharedGroupPan.setLayout(new GridLayout(0,3));
		
		
	
		JPanel buttonGroup=null;
		JRadioButton selectionRadBtn=null;
		ButtonGroup radioGroup=null;
		Map<Glyph,Glyph> nestMap = new HashMap<Glyph, Glyph>();
		
		for (Glyph i : intersectionTable.keySet()){//for each draged component
			buttonGroup=new JPanel();
			buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.Y_AXIS));
			buttonGroup.setBorder(BorderFactory.createTitledBorder(i.getName()));
			radioGroup = new ButtonGroup();
			
			for (Glyph j : intersectionTable.get(i)) {//for each entity it intersects with
				selectionRadBtn=new JRadioButton(j.getName());
				selectionRadBtn.addActionListener(FacadeController.getInstance().nestDialog_RadioListener(nestMap, i, j));
				buttonGroup.add(selectionRadBtn);
				radioGroup.add(selectionRadBtn);
			}
			selectionRadBtn.doClick();
			sharedGroupPan.add(buttonGroup);
		}
		JPanel okBtnPan = new JPanel();
		okBtnPan.setLayout(new BoxLayout(okBtnPan, BoxLayout.X_AXIS));
		
		JButton okBtn = new JButton("Ok");
		okBtnPan.add(okBtn);
		okBtn.addActionListener(FacadeController.getInstance().nestDialog_OkListener(nestMap, this));
		
		JButton cancelBtn = new JButton("Cancel");
		okBtnPan.add(cancelBtn);
		cancelBtn.addActionListener(FacadeController.getInstance().nestDialog_cancelBtnListener(this));
		 
		contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
		contentPane.add(messageLblPan);
		contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
		contentPane.add(sharedGroupPan);
		contentPane.add(okBtnPan);
		centerDialog();
	}
}
