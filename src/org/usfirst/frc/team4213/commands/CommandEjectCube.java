package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.Intake;

public class CommandEjectCube implements MCRCommand {
	private boolean firstTime = false;
	private final Intake intake = Intake.getInstance();

	@Override
	public void run() {
		if(firstTime) {
			intake.autoEject();
			firstTime = false;
		}
	}

	@Override
	public boolean isFinished() {
		return !intake.isIntakeRunning();
	}

}
