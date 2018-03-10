package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.lib14.UtilityMethods;
import org.usfirst.frc.team4213.robot.Robot;
import org.usfirst.frc.team4213.robot.RobotMap;

public abstract class AutoDrive {
	protected static final Logger logger = Logger.getLogger(Robot.class.getName());
	protected DriveTrain driveTrain = DriveTrain.getInstance();

	protected AutoDrive( ) {
		logger.setLevel(RobotMap.LogLevels.autoDriveClass);
	}
	
	protected enum State {
		IDLE, ACTIVE, DONE
	};

	protected State currentState = State.IDLE;
	protected PDController driveController;

	protected double limitCorrection(double correction, double maxAdjustment) {
		if (Math.abs(correction) > Math.abs(maxAdjustment))
			return UtilityMethods.copySign(correction, maxAdjustment);
		return correction;
	}

	public boolean isFinished() {
		return State.DONE == currentState;

	}

	public abstract void run();

}
