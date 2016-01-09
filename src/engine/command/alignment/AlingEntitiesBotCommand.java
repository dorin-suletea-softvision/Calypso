/*
 * Copyright (c) 2014 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package engine.command.alignment;

import java.awt.Rectangle;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

import sesion.Session;
import bridge.transferable.interfaces.CommandInterface;
import engine.views.interfaces.ViewDragableInterface;

/**
 * @author <a href="mailto:GameDEV@ssi-schaefer-noell.com">GameDEV</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

public class AlingEntitiesBotCommand implements CommandInterface {
	private ArrayList<ViewDragableInterface> alignedEntities;
	private ArrayList<Rectangle> boundsBeforeAlign;

	public AlingEntitiesBotCommand(Set<ViewDragableInterface> selectedEntities) {
		alignedEntities = new ArrayList<ViewDragableInterface>();
		boundsBeforeAlign = new ArrayList<Rectangle>();

		alignedEntities.addAll(selectedEntities);
		for (ViewDragableInterface view : alignedEntities) {
			boundsBeforeAlign.add(view.getBounds());
		}
	}

	@Override
	public boolean execute() throws InvalidParameterException {
		if (!isValid())
			return false;

		// get topmost item
		int maxY = 0;
		for (ViewDragableInterface view : alignedEntities) {
			if (view.getBounds().y > maxY) {
				maxY = view.getBounds().y;
			}
		}

		// offset others
		for (ViewDragableInterface view : alignedEntities) {
			Rectangle prevBounds = view.getBounds();
			view.setBounds(prevBounds.x, maxY, prevBounds.width, prevBounds.height);
		}

		Session.getSelectedSheet().update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
		return true;
	}

	@Override
	public void undo() {
		if (alignedEntities.size() != boundsBeforeAlign.size()) {
			throw new RuntimeException("Trying to undo align left , previous bounds array and view array are distinct sizes");
		}
		for (int i = 0; i < alignedEntities.size(); i++) {
			Rectangle bounds = boundsBeforeAlign.get(i);
			alignedEntities.get(i).setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		}

		Session.getSelectedSheet().update();
		Session.getSelectedSheet().repaint(Session.getSelectedSheet().getVisibleRect());
	}

	@Override
	public boolean isValid() {
		if (alignedEntities.size() == 0 || alignedEntities.size() < 2) {
			JOptionPane.showMessageDialog(Session.getSelectedSheet(), "There must be at lease 2 objects selected");
			return false;
		}
		return true;
	}
}
