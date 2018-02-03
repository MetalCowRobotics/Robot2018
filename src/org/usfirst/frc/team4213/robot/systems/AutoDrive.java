package org.usfirst.frc.team4213.robot.systems;

public class AutoDrive {
	protected DriveTrain driveTrain = DriveTrain.getInstance();

	protected enum State {
		IDLE, ACTIVE, DONE
	};

	protected State currentState = State.IDLE;

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
