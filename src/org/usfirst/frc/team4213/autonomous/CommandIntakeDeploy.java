package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.Intake;

public class CommandIntakeDeploy implements MCRCommand {
	private boolean firstTime = true;

	public CommandIntakeDeploy() {
	}

	@Override
	public void run() {
		if (firstTime) {
			Intake.getInstance().autoDeploy();
			firstTime = false;
		}
		System.out.println("<<<< Deploy >>>>");
	}

	@Override
	public boolean isFinished() {
		return Intake.getInstance().isIntakeDown();
	}

}
