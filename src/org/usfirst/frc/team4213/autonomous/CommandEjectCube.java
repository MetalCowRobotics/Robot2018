package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.Intake;

public class CommandEjectCube implements MCRCommand {
	private boolean firstTime = false;
	private final Intake intake = Intake.getInstance();

	public CommandEjectCube() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if(firstTime) {
			intake.autoEject();
			firstTime = false;
		}
		System.out.println("<<<< Eject Cube >>>>");
	}

	@Override
	public boolean isFinished() {
		return !intake.isIntakeRunning();
	}

}
