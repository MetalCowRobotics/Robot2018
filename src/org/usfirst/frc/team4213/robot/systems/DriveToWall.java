package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.RobotMap;

public class DriveToWall extends AutoDrive {
	
	private double howClose = 0;

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
			driveTrain.arcadeDrive(RobotMap.DriveToWall.TOP_SPEED, setPoint);
			currentState = State.ACTIVE;
			break;
		case ACTIVE:
			if (howClose > driveTrain.wallSensorInches()) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				if (howClose + RobotMap.DriveToWall.SLOW_DOWN_DISTANCE > driveTrain.wallSensorInches()) {
					driveTrain.arcadeDrive(RobotMap.DriveToWall.BOTTOM_SPEED, limitCorrection(correction, RobotMap.DriveToWall.MAX_ADJUSTMENT));
				} else {
					driveTrain.arcadeDrive(RobotMap.DriveToWall.TOP_SPEED, limitCorrection(correction, RobotMap.DriveToWall.MAX_ADJUSTMENT));
				}
				driveTrain.arcadeDrive(RobotMap.DriveToWall.TOP_SPEED, limitCorrection(correction, RobotMap.DriveToWall.MAX_ADJUSTMENT));
				System.out.println("Angle:" + driveTrain.getAngle());
				System.out.println("correction:" + correction);
			}
			break;
		case DONE:
			break;
		}
	}
}
