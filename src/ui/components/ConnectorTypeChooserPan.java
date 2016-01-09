package ui.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import static sesion.props.UIPropSheet.CONNECTOR_TYPE_PAN_GRID_H_CELLCOUNT;
import static sesion.props.UIPropSheet.CONNECTOR_TYPE_BTN_DIM;
import static sesion.props.UIPropSheet.CONNECTOR_TYPE_BTN_PAD;
import ui.controller.FacadeController;
import bridge.transferable.ConnectorContextTransferWraper;
import bridge.transferable.context.ConnectorContextInterface;

public class ConnectorTypeChooserPan extends JPanel {
	private static final long serialVersionUID = 1L;

	public ConnectorTypeChooserPan(List<ConnectorContextTransferWraper> model, ConnectorContextInterface thisContext) {
		super(); 
		setLayout(null);
		initComponents(model, thisContext);
		setPreferredSize(new Dimension((model.size() / CONNECTOR_TYPE_PAN_GRID_H_CELLCOUNT + 1)* 
									   (CONNECTOR_TYPE_BTN_DIM.width + CONNECTOR_TYPE_BTN_PAD), 
				
									    CONNECTOR_TYPE_PAN_GRID_H_CELLCOUNT * CONNECTOR_TYPE_BTN_DIM.height +
									    (CONNECTOR_TYPE_PAN_GRID_H_CELLCOUNT+2) * (CONNECTOR_TYPE_BTN_PAD)));
	}

	private void initComponents(List<ConnectorContextTransferWraper> model, ConnectorContextInterface thisContext) {
		int currentX = CONNECTOR_TYPE_BTN_PAD;
		int currentY = CONNECTOR_TYPE_BTN_PAD;
		int i = 0;

		ButtonGroup bg = new ButtonGroup();

		for (final ConnectorContextTransferWraper cmp : model) {
			i++;
			JToggleButton auxBt = new JToggleButton(cmp.getIcon());
			auxBt.setSize(CONNECTOR_TYPE_BTN_DIM);
			auxBt.setLocation(currentX, currentY);

			if (i < CONNECTOR_TYPE_PAN_GRID_H_CELLCOUNT)
				currentY += CONNECTOR_TYPE_BTN_PAD + auxBt.getHeight();
			else {
				i = 0;
				currentY = CONNECTOR_TYPE_BTN_PAD;
				currentX += CONNECTOR_TYPE_BTN_PAD + auxBt.getWidth();
			}

			if (cmp.getContext().getDrawnType().equals(thisContext.getDrawnType()))
				auxBt.setSelected(true);

			auxBt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FacadeController.getInstance().changeConnectorContext(cmp.getContext());
				}
			});
			add(auxBt);
			bg.add(auxBt);
		}
	}
}