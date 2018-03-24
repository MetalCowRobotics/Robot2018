package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.Elevator;

public class CommandRaiseElevator implements MCRCommand {
	private boolean firstTime = true;
	private double height;
	private Elevator elevator = Elevator.getInstance();
	private boolean atHeight = false;
	public CommandRaiseElevator(double heightInches) {
		height = heightInches;
	}

	@Override
	public void run() {
		if (firstTime) {
			firstTime = false;
			System.out.println("AM I RAISING THE ELEVATOR");
			Elevator.getInstance().setPosition(height);
			//elevator.setElevatorSpeed(-.3);
		}
		System.out.println("<<<< Raise Elevator >>>>");
//		if (elevator.getEncoderTics()>1300) {
//			elevator.setElevatorSpeed(-.1);
//			atHeight=true;
//		}
	}

	@Override
	public boolean isFinished() {
		return atHeight;
	}

}
