package ui.components;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sesion.props.UIPropSheet;

public class DialogHeader extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private final  int PAD_H ;
	private final  int PAD_V ;
	
	private JLabel titleLbl;
	private JLabel iconLbl;
	private JLabel descriptionLbl;
	private LineLabel lineLbl;

	public DialogHeader(int pad_h,int pad_v) {
		PAD_H=pad_h;
		PAD_V=pad_v;
		addComponents();
		setPreferredSize((UIPropSheet.DIAG_HEADER_FOOTER_DIM));
	}
	
	private void addComponents(){
		setLayout(null);
	
		titleLbl = new JLabel();
//		titleLbl.setFont(titleLbl.getFont().deriveFont(20));
		titleLbl.setFont(titleLbl.getFont().deriveFont((float)20).deriveFont(Font.BOLD));
		
		
		titleLbl.setBounds(PAD_V, 
							PAD_H,
							UIPropSheet.DIAG_HEADER_FOOTER_DIM.width/2,
							UIPropSheet.DIAG_TITLE_H
							);
		
		add(titleLbl);
		
		iconLbl = new JLabel();
		iconLbl.setBounds(UIPropSheet.DIAG_HEADER_FOOTER_DIM.width-UIPropSheet.DIAG_IMG_DIM.width-PAD_V, 
							PAD_H, 
							UIPropSheet.DIAG_IMG_DIM.width,
							UIPropSheet.DIAG_IMG_DIM.height);
		add(iconLbl);
		
		
		descriptionLbl = new JLabel();
		descriptionLbl.setBounds(titleLbl.getX(), titleLbl.getY()+titleLbl.getHeight()+PAD_H, titleLbl.getWidth(), titleLbl.getHeight());
		add(descriptionLbl);
		
		lineLbl = new LineLabel(UIPropSheet.DIAG_HEADER_FOOTER_DIM.width-PAD_V*2);
		lineLbl.setLocation(PAD_V, iconLbl.getY()+iconLbl.getHeight()+PAD_H);

		add(lineLbl);
	}
	
	public void initComponents(String tile,String description,ImageIcon image){
		titleLbl.setText(tile);	
		descriptionLbl.setText(description);
		iconLbl.setIcon(image);
	}
	
	public void setTitle(String newTitle){
		titleLbl.setText(newTitle);
	}
	
	public void setDescription(String newDescription){
		descriptionLbl.setText(newDescription);
	}
	
	

}
