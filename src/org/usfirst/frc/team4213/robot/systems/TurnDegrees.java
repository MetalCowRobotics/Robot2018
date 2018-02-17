package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.lib14.UtilityMethods;
import org.usfirst.frc.team4213.robot.RobotMap;

public class TurnDegrees extends AutoDrive {
	private double degrees;
	
	public TurnDegrees(double degrees) {
		super();
		this.degrees = degrees;
	}

	public void run() {
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			double setPoint = driveTrain.getAngle();
			driveController = new PDController(setPoint + degrees);
			driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, driveController.calculateAdjustment(setPoint));
			currentState = State.ACTIVE;
			break;
		case ACTIVE:
			double currentAngle = driveTrain.getAngle();
			if (UtilityMethods.between(currentAngle, degrees - RobotMap.TurnDegrees.VARIANCE, degrees + RobotMap.TurnDegrees.VARIANCE)) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
				if (UtilityMethods.between(currentAngle, degrees - RobotMap.TurnDegrees.SLOW_VARIANCE, degrees + RobotMap.TurnDegrees.SLOW_VARIANCE)) {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.SLOW_ADJUSTMENT));
					System.out.println("slow");
				} else {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
					System.out.println("fast");

				}
				System.out.println("Angle:" + driveTrain.getAngle());
				System.out.println("correction:" + correction);
			}
			break;
		case DONE:
			break;
		}

	}
}