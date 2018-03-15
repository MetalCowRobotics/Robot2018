package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;

public class CommandDriveStraight implements MCRCommand {
	private DriveWithEncoder thisCommand;

	public CommandDriveStraight(double inches) {
		thisCommand = new DriveWithEncoder(inches);
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