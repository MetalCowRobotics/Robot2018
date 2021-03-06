package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.Intake;

public class CommandIntakeDeploy implements MCRCommand {
	private boolean firstTime = true;

	@Override
	public void run() {
		if (firstTime) {
			System.out.println("ARE WE DEPLOYING????");
			Intake.getInstance().autoDeploy();
			firstTime = false;
		}
	}

	@Override
	public boolean isFinished() {
		return !firstTime;
	}

}
