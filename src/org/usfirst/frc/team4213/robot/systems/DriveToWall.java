package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;

public class DriveToWall extends AutoDrive {
	
	private double howClose = 0;
	private double baseSpeed = .6;
	private double slowdownDistance = 12;
	private double slowSpeed = 0.4;
	private final double maxAdjustment = .4;

	public DriveToWall(double howClose) {
		super();
		this.howClose = howClose;
	}

	public void run() {
		System.out.println("Distance:" + driveTrain.wallSensorInches());
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			double setPoint = driveTrain.getAngle();
			driveController = new PDController(setPoint);
			driveTrain.arcadeDrive(baseSpeed, setPoint);
			currentState = State.ACTIVE;
			break;
		case ACTIVE:
			if (howClose > driveTrain.wallSensorInches()) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				if (howClose + slowdownDistance > driveTrain.wallSensorInches()) {
					driveTrain.arcadeDrive(slowSpeed, limitCorrection(correction, maxAdjustment));
				} else {
					driveTrain.arcadeDrive(baseSpeed, limitCorrection(correction, maxAdjustment));
				}
				driveTrain.arcadeDrive(baseSpeed, limitCorrection(correction, maxAdjustment));
				System.out.println("Angle:" + driveTrain.getAngle());
				System.out.println("correction:" + correction);
			}
			break;
		case DONE:
			break;
		}
	}
}
