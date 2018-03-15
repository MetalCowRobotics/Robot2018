package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.RobotMap;

public class DriveToWall extends AutoDrive {
	private double howClose = 0;

	public DriveToWall(double howClose) {
		super();
		this.howClose = howClose;
		logger.info("howClose:" + howClose);
	}

	public void run() {
		logger.info("Distance to wall:" + driveTrain.wallSensorInches());
		switch (currentState) {
		case IDLE:
			logger.fine("Idle");
			driveTrain.resetGyro();
			double setPoint = driveTrain.getAngle();
			logger.info("setPoint:" + setPoint);
			driveController = new PDController(setPoint);
			driveTrain.arcadeDrive(RobotMap.DriveToWall.TOP_SPEED, setPoint);
			currentState = State.ACTIVE;
			break;
		case ACTIVE:
			logger.fine("Active");
			if (howClose > driveTrain.wallSensorInches()) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				logger.fine("angle:" + driveTrain.getAngle());
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				logger.fine("correction:" + correction);
				if (howClose + RobotMap.DriveToWall.SLOW_DOWN_DISTANCE > driveTrain.wallSensorInches()) {
					driveTrain.arcadeDrive(RobotMap.DriveToWall.BOTTOM_SPEED,
							limitCorrection(correction, RobotMap.DriveToWall.MAX_ADJUSTMENT));
					logger.fine("slow down");
				} else {
					driveTrain.arcadeDrive(RobotMap.DriveToWall.TOP_SPEED,
							limitCorrection(correction, RobotMap.DriveToWall.MAX_ADJUSTMENT));
				}
				// driveTrain.arcadeDrive(RobotMap.DriveToWall.TOP_SPEED,
				// limitCorrection(correction, RobotMap.DriveToWall.MAX_ADJUSTMENT));
			}
			break;
		case DONE:
			logger.fine("Done");
			break;
		}
	}
}
