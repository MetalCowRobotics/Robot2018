package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;

public class AutoDrive {
	protected DriveTrain driveTrain = DriveTrain.getInstance();

	protected enum State {
		IDLE, ACTIVE, DONE
	};

	protected State currentState = State.IDLE;
	protected PDController driveController;

	protected double limitCorrection(double correction, double maxAdjustment) {
		if (Math.abs(correction) > Math.abs(maxAdjustment))
			if (correction < 0)
				return -maxAdjustment;
			else
				return maxAdjustment;
		return correction;
	}

	public boolean isFinished() {
		return State.DONE == currentState;

	}
}
