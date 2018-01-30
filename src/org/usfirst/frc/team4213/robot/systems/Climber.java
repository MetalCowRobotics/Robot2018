package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.controllers.MasterControls;

public class Climber {
	private static final Climber instance = new Climber();
	private static final Logger logger = Logger.getLogger(Climber.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();

	// TODO: Motors

	// TODO: Sensors

	private Climber() {
		// Singleton
	}

	public static Climber getinstance() {
		return instance;
	}

	public void execute() {

	}

}
