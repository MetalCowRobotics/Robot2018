package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.DriveStraightTime;

public class CommandDriveTime implements MCRCommand{
	private DriveStraightTime thisCommand;

	public CommandDriveTime(double seconds) {
		thisCommand = new DriveStraightTime(seconds);
	}

	@Override
	public void run() {
		thisCommand.run();
	}

	@Override
	public boolean isFinished() {
		return thisCommand.isFinished();
	}
}
