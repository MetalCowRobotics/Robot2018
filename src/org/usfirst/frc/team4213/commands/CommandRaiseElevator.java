package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.Elevator;

public class CommandRaiseElevator implements MCRCommand {
	private boolean firstTime = true;
	private double height;

	public CommandRaiseElevator(double heightInches) {
		height = heightInches;
	}

	@Override
	public void run() {
		if (firstTime) {
			firstTime = false;
			Elevator.getInstance().setPosition(height);
		}
		System.out.println("<<<< Raise Elevator >>>>");
	}

	@Override
	public boolean isFinished() {
		return Elevator.getInstance().isAtHeight(height);
	}

}
