package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.Timer;

public class CommandTurn implements MCRCommand {
	private TurnDegrees thisCommand;
	private boolean firstTime = true;
	private Timer timer = new Timer();
	private double expectedTime = 5;
	public CommandTurn(double turnDegrees) {
		
		thisCommand = new TurnDegrees(turnDegrees);
	}
	
	public CommandTurn(double turnDegrees, double seconds) {
		
		thisCommand = new TurnDegrees(turnDegrees);
		expectedTime = seconds;
	}

	@Override
	public void run() {
		thisCommand.run();
		if (firstTime) {
			firstTime = false;
			timer.reset();
			timer.start();
		}
	}

	@Override
	public boolean isFinished() {
		if(firstTime) {
			return false;
		}
		if (timer.get()>expectedTime) {
			return true;
		}
		return thisCommand.isFinished();	
	}

}
