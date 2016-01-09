/*
 * Copyright (c) 2014 SSI Schaefer Noell GmbH
 *
 * $Header: $
 */

package ui.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import sesion.Session;
import sesion.props.UIPropSheet;
import ui.interfaces.LocalToolbarType;
import ui.resources.ResourceRetriever;
import bridge.transferable.interfaces.CommandInterface;
import engine.command.alignment.AlignEntitiesHorizontalSpaceCommand;
import engine.command.alignment.AlignEntitiesVerticalSpaceCommand;
import engine.command.alignment.AlingEntitiesBotCommand;
import engine.command.alignment.AlingEntitiesLeftCommand;
import engine.command.alignment.AlingEntitiesRightCommand;
import engine.command.alignment.AlingEntitiesTopCommand;

/**
 * @author <a href="mailto:GameDEV@ssi-schaefer-noell.com">GameDEV</a>
 * @version $Revision: $, $Date: $, $Author: $
 */

public class EditToolbar extends LocalToolbarType {
	private static final long serialVersionUID = 1L;
	private JButton alignLeftBtn;
	private JButton alignRightBtn;
	private JButton alignTopBtn;
	private JButton alignBotBtn;

	private JButton alignHorizontalSpaceBtn;
	private JButton alignVerticalSpaceBtn;

	public EditToolbar() {
		addComponents();
		addListeners();
		setAlignmentX(0);
	}

	private void addComponents() {
		alignLeftBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.ALIGN_LEFT_ICON_IMG));
		alignLeftBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		this.add(alignLeftBtn);

		alignRightBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.ALIGN_RIGHT_ICON_IMG));
		alignRightBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		this.add(alignRightBtn);

		alignTopBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.ALIGN_TOP_ICON_IMG));
		alignTopBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		this.add(alignTopBtn);

		alignBotBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.ALIGN_BOT_ICON_IMG));
		alignBotBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		this.add(alignBotBtn);

		alignVerticalSpaceBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.ALIGN_VERTICAL_SPACE_ICON_IMG));
		alignVerticalSpaceBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		this.add(alignVerticalSpaceBtn);

		alignHorizontalSpaceBtn = new JButton(ResourceRetriever.loadImage(ResourceRetriever.ALIGN_HORIZONTAL_SPACE_ICON_IMG));
		alignHorizontalSpaceBtn.setPreferredSize(UIPropSheet.MEDIUM_BTN_DIM);
		this.add(alignHorizontalSpaceBtn);

	}

	private void addListeners() {
		alignLeftBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				CommandInterface alignLeftCommand = new AlingEntitiesLeftCommand(Session.getSelectedDragableViews());
				if (alignLeftCommand.execute())
					Session.addCommand(alignLeftCommand);

			}
		});

		alignVerticalSpaceBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				CommandInterface alignVerticalSpaceCommand = new AlignEntitiesVerticalSpaceCommand(Session.getSelectedDragableViews());
				if (alignVerticalSpaceCommand.execute())
					Session.addCommand(alignVerticalSpaceCommand);
			}
		});

		alignRightBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				CommandInterface alignRightCommand = new AlingEntitiesRightCommand(Session.getSelectedDragableViews());
				if (alignRightCommand.execute())
					Session.addCommand(alignRightCommand);
			}
		});

		alignTopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				CommandInterface alignRightCommand = new AlingEntitiesTopCommand(Session.getSelectedDragableViews());
				if (alignRightCommand.execute())
					Session.addCommand(alignRightCommand);
			}
		});

		alignBotBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				CommandInterface alignRightCommand = new AlingEntitiesBotCommand(Session.getSelectedDragableViews());
				if (alignRightCommand.execute())
					Session.addCommand(alignRightCommand);
			}
		});

		alignHorizontalSpaceBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				CommandInterface alignRightCommand = new AlignEntitiesHorizontalSpaceCommand(Session.getSelectedDragableViews());
				if (alignRightCommand.execute())
					Session.addCommand(alignRightCommand);
			}
		});

	}
}
