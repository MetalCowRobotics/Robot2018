package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.Timer;

public class CommandPause implements MCRCommand {
	private boolean firstTime = true;
	private Timer timer = new Timer();
	private double expectedTime = 5;

	public CommandPause(double seconds) {
		expectedTime = seconds;
	}

	@Override
	public void run() {
		if (firstTime) {
			firstTime = false;
			timer.reset();
			timer.start();
		}
	}

	@Override
	public boolean isFinished() {
		if (firstTime) {
			return false;
		}
		if (timer.get() > expectedTime) {
			timer.stop();
			return true;
		}
		return false;
	}

}
