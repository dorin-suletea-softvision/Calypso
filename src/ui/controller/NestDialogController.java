package ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JDialog;

import bridge.transferable.interfaces.CommandInterface;

import sesion.Session;
import engine.command.NestCommand;
import engine.command.SequenceCommand;
import engine.views.Glyph;

public class NestDialogController {
	
	public ActionListener radBtnListener(final Map<Glyph, Glyph> nestTable,  final Glyph nestable ,  final Glyph nesting){	
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nestTable.put(nestable, nesting);
			}
		};
	}
	
	public ActionListener okBtnListener(final Map<Glyph, Glyph> nestTable,final JDialog windowToClose){
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<CommandInterface> commands = new ArrayList<CommandInterface>();
				for (Glyph g : nestTable.keySet())
					commands.add(new NestCommand(nestTable.get(g), g, (Glyph)g.getParent()));

				CommandInterface command = new SequenceCommand(commands);
				command.execute();
				Session.addCommand(command);
				windowToClose.dispose();
			}
		};
	}
	
	public ActionListener cancelBtnListener(final JDialog windowToClose){
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Session.undoAndRemove();
				windowToClose.dispose();
				
			}
		};
	}
}
