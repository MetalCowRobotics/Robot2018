package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.DriveBackwardWithEncoder;

//import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder; 
import edu.wpi.first.wpilibj.Timer;

public class CommandDriveBackwards implements MCRCommand {
	private DriveBackwardWithEncoder thisCommand;
	private Timer timer = new Timer();
	private boolean firstTime = true;
	private double targetTime = 6;

	public CommandDriveBackwards(double inches, double expectedTime) {
		thisCommand = new DriveBackwardWithEncoder(inches);
		targetTime = expectedTime;
	}

	public CommandDriveBackwards(double inches) {
		thisCommand = new DriveBackwardWithEncoder(inches);
	}

	@Override
	public void run() {
		if (firstTime) {
			firstTime = false;
			timer.reset();
			timer.start();
		}
		thisCommand.run();
	}

	@Override
	public boolean isFinished() {
		if (timer.get() > targetTime) {
			return true;
		}
		return thisCommand.isFinished();
	}
}