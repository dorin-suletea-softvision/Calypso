package engine.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import sesion.Session;
import sesion.props.UIPropSheet;
import ui.components.ConnectorTypeChooserPan;
import ui.components.EmptyControlPann;
import ui.components.MultilineLabel;
import ui.resources.ResourceRetriever;
import bridge.exceptions.ComlianceException;
import bridge.transferable.ConnectorContextTransferWraper;
import bridge.transferable.context.ConnectorContextInterface;
import bridge.transferable.interfaces.AbstractControlJPanel;
import bridge.transferable.interfaces.ConnectorModelInterface;
import bridge.transferable.proxy.ConnectorViewProxy;
import engine.views.interfaces.ViewFocusableInterface;

public class LabeledConnectionView extends ConnectionView implements ConnectorViewProxy, ViewFocusableInterface {
	private MultilineLabel startLBL;
	private MultilineLabel midLBL;
	private MultilineLabel endLBL;

	public LabeledConnectionView(ConnectorModelInterface model,SheetView host) throws ComlianceException{
		super(model,host);
		startLBL = new MultilineLabel();
		midLBL = new MultilineLabel();
		endLBL = new MultilineLabel();
		
		startLBL.setText(model.getStartText());
		midLBL.setText(model.getMidText());
		endLBL.setText(model.getEndText());
		
		Session.getSelectedSheet().add(startLBL);
		Session.getSelectedSheet().add(midLBL);
		Session.getSelectedSheet().add(endLBL);
		
		update();
	}
	
	public LabeledConnectionView(EntityView origin, EntityView end) {
		super(origin, end);
		startLBL = new MultilineLabel();
		midLBL = new MultilineLabel();
		endLBL = new MultilineLabel();
		Session.getSelectedSheet().add(startLBL);
		Session.getSelectedSheet().add(midLBL);
		Session.getSelectedSheet().add(endLBL);
		update();
	}
	
	public void setContext(ConnectorContextInterface context){
		setStartText("");
		setMidText("");
		setEndText("");
		
		if (context.equals(getContext())){
			System.out.println("Equal contexts on swap-- Skipion operation");
			return;
		}
		if (this.getContext().isLabeled() && !context.isLabeled()){
			System.out.println("Swaping  Labeled connector to non-Labeled connector");
			Session.getSelectedSheet().remove(startLBL);
			Session.getSelectedSheet().remove(midLBL);
			Session.getSelectedSheet().remove(endLBL);
		}
		if (!this.getContext().isLabeled() && context.isLabeled()){
			System.out.println("Swaping  non-Labeled connector to Labeled connector");
			Session.getSelectedSheet().add(startLBL);
			Session.getSelectedSheet().add(midLBL);
			Session.getSelectedSheet().add(endLBL);
		}
		
		
		super.setContext(context);
	}

	@Override
	public void update() {
		super.update();
		if (startLBL != null && midLBL != null && endLBL != null && getContext().isLabeled()) {
			startLBL.update();
			midLBL.update();
			endLBL.update();
			arrangeStartLBL();
			arrangeMidLBL();
			arrangeEndLBL();
		}
	}

	private void arrangeStartLBL() {
		if (getSnapCount() == 0) {
			Point2D mid = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(super.getStartPoint(), super.getEndPoint()));
			Point2D location = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(super.getStartPoint(), mid));
			startLBL.setLocation((int) location.getX() - startLBL.getWidth() / 2, (int) location.getY() - startLBL.getHeight() / 2);
		}
		else if (getSnapCount() > 0) {
			Point2D location = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(super.getStartPoint(), getSnap(0).getLocation()));
			startLBL.setLocation((int) location.getX() - startLBL.getWidth() / 2, (int) location.getY());
		}
	}

	private void arrangeMidLBL() {
		if (getSnapCount() == 0) {
			Point2D location = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(super.getStartPoint(), super.getEndPoint()));
			midLBL.setLocation((int) location.getX() - midLBL.getWidth() / 2, (int) location.getY() - midLBL.getHeight() / 2);
		}
		else if (getSnapCount() == 1) {
			midLBL.setLocation(getSnap(0).getX() - midLBL.getWidth() / 2 + getSnap(0).getWidth() / 2, getSnap(0).getY() + getSnap(0).getHeight());
		}
		else if (getSnapCount() % 2 == 1) {
			midLBL.setLocation(getSnap(getSnapCount() / 2).getX() - midLBL.getWidth() / 2 + getSnap(0).getWidth() / 2, getSnap(getSnapCount() / 2).getY() + getSnap(0).getHeight());
		}
		else {
			Point2D location = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(getSnap(getSnapCount() / 2).getLocation(), getSnap(getSnapCount() / 2 - 1).getLocation()));
			midLBL.setLocation((int) location.getX() - midLBL.getWidth() / 2+ getSnap(0).getWidth() / 2, (int) location.getY() - midLBL.getHeight() / 2 + getSnap(0).getWidth() / 2);
		}
	}

	private void arrangeEndLBL() {
		if (getSnapCount() == 0) {
			Point2D mid = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(super.getStartPoint(), super.getEndPoint()));
			Point2D location = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(super.getEndPoint(), mid));
			endLBL.setLocation((int) location.getX() - endLBL.getWidth() / 2, (int) location.getY() - endLBL.getHeight() / 2);
		}
		else if (getSnapCount() > 0) {
			Point2D location = Session.getIntersectionAlgorithm().lineMidPoint(new Line2D.Float(super.getEndPoint(), getSnap(getSnapCount() - 1).getLocation()));
			endLBL.setLocation((int) location.getX() - endLBL.getWidth() / 2, (int) location.getY());
		}
	}

	@Override
	public AbstractControlJPanel generateControlPan() {
		List<ConnectorContextTransferWraper> contexts = Session.getPluginBySignature(Session.getSelectedSheet().getPluginSignature()).getConnectorContexts();
		contexts.add(0, new ConnectorContextTransferWraper(new DefaultConnectorContext(), ResourceRetriever.loadImage(ResourceRetriever.DEFAULT_CONNECTOR_CONTEXT_BTN_IMG)));

		EmptyControlPann innerRet = new EmptyControlPann();
		innerRet.setLayout(new BorderLayout());
		ConnectorTypeChooserPan chooser = new ConnectorTypeChooserPan(contexts, this.getContext());

		JScrollPane sp = new JScrollPane(chooser);
		sp.setBorder(BorderFactory.createTitledBorder("Connector types"));
		innerRet.add(sp, BorderLayout.NORTH);
		
		AbstractControlJPanel controlPanel = getContext().generateUiPan(this);
		
		innerRet.setPreferredSize(new Dimension(innerRet.getPreferredSize().width,sp.getPreferredSize().height+controlPanel.getPreferredSize().height));
		innerRet.add(controlPanel,BorderLayout.CENTER);

		sp.setPreferredSize(new Dimension(sp.getPreferredSize().width - UIPropSheet.SCROLL_BAR_W * 2, UIPropSheet.CONNECTOR_TYPE_SP_PREF_H));
		innerRet.revalidate();

		return innerRet;
	}

	@Override
	public void setStartText(String text) {
		startLBL.setText(text);
		super.getModel().setStartText(text);
		update();
	}

	@Override
	public void setMidText(String text) {
		midLBL.setText(text);
		super.getModel().setMidText(text);
		update();
	}

	@Override
	public void setEndText(String text) {
		endLBL.setText(text);
		super.getModel().setEndText(text);
		update();
	}

	@Override
	public String getType() {
		return getModel().getDrawnType();
	}

	@Override
	public String getSourceEntityName() {
		return super.getOriginEntity().getModel().getName();
	}

	@Override
	public String getEndEntityName() {
		return super.getEndEntity().getModel().getName();
	}

	@Override
	public String getStartText() {
		return getModel().getStartText();
	}

	@Override
	public String getMidText() {
		return getModel().getMidText();
	}

	@Override
	public String getEndText() {
		return getModel().getEndText();
	}
	
	public void delete(){
		Session.getSelectedSheet().remove(startLBL);
		Session.getSelectedSheet().remove(midLBL);
		Session.getSelectedSheet().remove(endLBL);
		super.delete();
	}
	
	public void restore(Glyph parent){
		super.restore(parent);
		Session.getSelectedSheet().add(startLBL);
		Session.getSelectedSheet().add(midLBL);
		Session.getSelectedSheet().add(endLBL);
	}

	@Override
	public Glyph getParrent() {
		System.out.println("Dummy return Sessio.getSelectedSheet for connectors , method not used");
		return Session.getSelectedSheet();
	}

}
