package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;

public class CommandDriveToObject implements MCRCommand {
	private DriveToWall command;

	public CommandDriveToObject(double inchesAway) {
		command = new DriveToWall(inchesAway);
	}

	@Override
	public void run() {
		command.run();
	}

	@Override
	public boolean isFinished() {
		return command.isFinished();
	}

}
