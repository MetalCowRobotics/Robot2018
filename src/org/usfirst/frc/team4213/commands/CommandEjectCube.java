package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.Intake;

public class CommandEjectCube implements MCRCommand {
	private boolean firstTime = true;
	private final Intake intake = Intake.getInstance();

	@Override
	public void run() {
		if(firstTime) {
			System.out.println("Starting the eject");
			intake.autoEject();
			firstTime = false;
		}
	}

	@Override
	public boolean isFinished() {
		return !firstTime;
//				intake.isIntakeRunning();
	}

}
