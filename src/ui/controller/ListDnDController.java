package ui.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import sesion.Session;
import ui.components.EntityContextListItem;
import ui.forms.MainForm;
import bridge.transferable.context.EntityContextInterface;
import bridge.transferable.interfaces.CommandInterface;
import engine.command.AddEntityCommand;
import engine.views.EntityView;
import engine.views.Glyph;

public class ListDnDController {
	private EntityContextInterface targetedContext; // used by list dnd
													// temporary storage

	public MouseAdapter getEntityListDND() {
		MouseAdapter ret = new MouseAdapter() {

			public void mouseDragged(MouseEvent e) {
				if (targetedContext == null)
					return;
				Dimension contextDimension = targetedContext.getDefaultSize();
				Session.getSelectedSheet().getGlassPane().clearEntityRectangles();
				Point p = SwingUtilities.convertPoint((JList<?>) e.getSource(), e.getPoint(), Session.getSelectedSheet());
				p.x = p.x - targetedContext.getDefaultSize().width / 2;
				p.y = p.y - targetedContext.getDefaultSize().height / 2;
				if (p.x < 0)
					p.x = 0;
				if (p.y < 0)
					p.y = 0;

				Session.getSelectedSheet().getGlassPane().addEntityRectangle(new Rectangle(p.x, p.y, contextDimension.width, contextDimension.height));
				Session.getSelectedSheet().repaint();

			}

			@SuppressWarnings("unchecked")
			@Override
			public void mousePressed(MouseEvent e) {
				EntityContextInterface targetedContext = null;
				if (e.getComponent() instanceof JList<?>) {
					JList<EntityContextListItem> src = (JList<EntityContextListItem>) e.getComponent();
					if (src.getSelectedIndex() != -1)
						targetedContext = src.getModel().getElementAt(src.getSelectedIndex()).getContext();
				}
				ListDnDController.this.targetedContext = targetedContext;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (targetedContext == null)
					return;
				// if click occurs , not dnd , and mouse is released on the list
				// discard the event
				if (e.getSource() instanceof JComponent && ((JComponent) e.getSource()).contains(e.getPoint())) {
					targetedContext = null;
					Session.getSelectedSheet().getGlassPane().clearEntityRectangles();
					MainForm.getInstance().repaint();
					return;
				}

				Session.getSelectedSheet().getGlassPane().clearEntityRectangles();
				Point p = SwingUtilities.convertPoint((JList<?>) e.getSource(), e.getPoint(), Session.getSelectedSheet());
				p.x = p.x - targetedContext.getDefaultSize().width / 2;
				p.y = p.y - targetedContext.getDefaultSize().height / 2;
				if (p.x < 0)
					p.x = 0;
				if (p.y < 0)
					p.y = 0;

				EntityView entToAdd = new EntityView(targetedContext, p);

				CommandInterface addEntCmd = new AddEntityCommand(entToAdd);
				if (addEntCmd.execute())
					Session.addCommand(addEntCmd);

				targetedContext = null;

				// Hashtable<Glyph, List<Glyph>> nestTable=new Hashtable<Glyph,
				// List<Glyph>>();
				ArrayList<Glyph> intersectionList = entToAdd.createIntersectionList();
				if (entToAdd.isNestingEnabled()) {
					throw new RuntimeException("Feature not suported");
					// nest procedure
					// if (!intersectionList.isEmpty()){
					// NestChoiceDialog diag = new NestChoiceDialog();
					// nestTable.put(entToAdd, intersectionList);
					// diag.initComponents(nestTable);
					// diag.setVisible(true);
					// }
					// !!
				}
				else {
					// reorder procedure
					intersectionList.add(entToAdd);

					Collections.sort(intersectionList, new Comparator<Glyph>() {
						@Override
						public int compare(Glyph o1, Glyph o2) {
							if (o1.getWidth() * o1.getHeight() > o2.getWidth() * o2.getHeight())
								return -1;
							if (o1.getWidth() * o1.getHeight() < o2.getWidth() * o2.getHeight())
								return 1;
							return 0;
						};
					});
					//due to the delay in event handling , if deleting an entity and adding another on the place where it was
					//swing removes the entity from the sheet , but the event has it's intersectionList already initialized 
					//and resoults : deleted cmp intersects new cmp , deletedCmp getParrent==null , thus NullPointer 
					//exception
					
//					System.out.println("-------------");
//					System.out.print("Components : ");
//					for (Component c : Session.getSelectedSheet().getComponents())
//						System.out.print(c+ " ");
//					System.out.println();
//					System.out.print("Glyphs : ");
//					System.out.print(Session.getSelectedSheet().getAllGlyphs());
//					
//					System.out.println();
//					System.out.print("Intersectlist : ");
//					System.out.println(intersectionList);

					for (int i = 0; i < intersectionList.size(); i++) {
							intersectionList.get(i).getParent().setComponentZOrder(intersectionList.get(i), i);
			
						}
				}
			}
		};
		return ret;
	}
}
