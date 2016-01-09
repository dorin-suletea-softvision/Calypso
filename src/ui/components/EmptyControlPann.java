package ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;

import ui.resources.ResourceRetriever;

import bridge.transferable.interfaces.AbstractControlJPanel;


public class EmptyControlPann extends AbstractControlJPanel{
	private static final long serialVersionUID = 1L;
	
	public EmptyControlPann() {
		super();
		setPreferredSize(new Dimension(190,400));
		setLayout(new BorderLayout());
		add(new JLabel(ResourceRetriever.loadImage(ResourceRetriever.APPLOGO_BIG)),BorderLayout.CENTER);
		
	}
	
	@Override
	public void cleanup() {
	}

	@Override
	public Object getModelObject() {
		return new Object();
	}

	@Override
	public void initComponents() {
		System.out.println("Dummy init components on EmptyControlPanenel");
	}
}
