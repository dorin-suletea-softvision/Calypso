package ui.controller;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import ui.components.Joystick;

public class JoystickUpdateController {
	public ComponentAdapter getUpdaterEvtListener(final Joystick joystick){
		return new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				joystick.updateBounds();
			}
		};
	}
}
