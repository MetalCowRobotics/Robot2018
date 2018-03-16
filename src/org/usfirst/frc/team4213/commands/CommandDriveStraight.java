package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;

import edu.wpi.first.wpilibj.Timer;

public class CommandDriveStraight implements MCRCommand {
	private DriveWithEncoder thisCommand;
	
	private Timer timer = new Timer();
	private boolean firstTime = true;
	private int targetTime = 10;
	
	public CommandDriveStraight(double inches, int expectedTime) {
		thisCommand = new DriveWithEncoder(inches);
		targetTime = expectedTime;
	}
	
	public CommandDriveStraight(double inches) {
		thisCommand = new DriveWithEncoder(inches);
	}

	@Override
	public void run() {
		if (firstTime) {
			firstTime=false;
			timer.reset();
			timer.start();
		}
		thisCommand.run();
	}

	@Override
	public boolean isFinished() {
		if(timer.get()>10) {
			return true;
		}
		return thisCommand.isFinished();		
	}

}
