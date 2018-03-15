package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.lib14.UtilityMethods;
import org.usfirst.frc.team4213.robot.HamburgerDashboard;
import org.usfirst.frc.team4213.robot.RobotMap;

public class TurnDegrees extends AutoDrive {
	private static final HamburgerDashboard dashboard = HamburgerDashboard.getInstance();
	private double degrees;
	private double setPoint;
	private int rechecks = 0;
	
	public TurnDegrees(double degrees) {
		super();
		this.degrees = degrees;
	}

	public void run() {
		dashboard.pushPID(driveController);
		dashboard.pushGyro();
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			setPoint = driveTrain.getAngle() + degrees;
			driveController = new PDController(setPoint, dashboard.getTurnKP(), dashboard.getTurnKI());
			driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, driveController.calculateAdjustment(setPoint));
			currentState = State.ACTIVE;
			logger.info("Target Degrees:" + setPoint);
			break;
		case ACTIVE:
			driveController.setkP(dashboard.getTurnKP());
			driveController.setkD(dashboard.getTurnKD());
			double currentAngle = driveTrain.getAngle();
			double correction = driveController.calculateAdjustment(driveTrain.getAngle());
			if (Math.abs(setPoint-currentAngle) < RobotMap.TurnDegrees.VARIANCE) {
				logger.info("======== turn on target !!! =========");
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				//driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
				if (UtilityMethods.between(currentAngle, setPoint - RobotMap.TurnDegrees.SLOW_VARIANCE, setPoint + RobotMap.TurnDegrees.SLOW_VARIANCE)) {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED,
							limitCorrection(correction, RobotMap.TurnDegrees.SLOW_ADJUSTMENT));
					System.out.println("slow");
				} else {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED,
							limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
					System.out.println("fast");

				}
				logger.info("Current Angle:" + driveTrain.getAngle());
				logger.info("correction:" + correction);
			}
			break;
		case RECHECK:
			if (rechecks < 3) {
				rechecks += 1;
				if (Math.abs(setPoint - driveTrain.getAngle()) < RobotMap.TurnDegrees.VARIANCE) {
					logger.info("======== turn on target !!! =========");
					driveTrain.stop();
					currentState = State.DONE;
				} else {
					currentState = State.ACTIVE;
				}
			} else {
				logger.info("======== tried enough =========");
				driveTrain.stop();
				currentState = State.DONE;
			}
			break;
		case DONE:
			break;
		}

	}
}