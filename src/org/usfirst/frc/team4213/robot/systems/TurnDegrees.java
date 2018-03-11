package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.lib14.UtilityMethods;
import org.usfirst.frc.team4213.robot.RobotMap;

public class TurnDegrees extends AutoDrive {
	private double degrees;
	private double setPoint;
	
	public TurnDegrees(double degrees) {
		super();
		this.degrees = degrees;
	}

	public void run() {
		dashboard.pushGyro();
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			setPoint = driveTrain.getAngle() + degrees; 
			logger.info("TurnDegrees SetPoint:" + setPoint);
			driveController = new PDController(setPoint, dashboard.getTurnKP(), dashboard.getTurnKI()); 
			driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, driveController.calculateAdjustment(setPoint));
			currentState = State.ACTIVE;
			break;
		case ACTIVE:
			driveController.set_kP(dashboard.getTurnKP()); 
			driveController.set_kD(dashboard.getTurnKD()); 
			double currentAngle = driveTrain.getAngle();
			if (Math.abs(setPoint-currentAngle) < RobotMap.TurnDegrees.VARIANCE) { 
				logger.info("======== turn on target !!! ========="); 
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				//driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
				if (UtilityMethods.between(currentAngle, setPoint - RobotMap.TurnDegrees.SLOW_VARIANCE, setPoint + RobotMap.TurnDegrees.SLOW_VARIANCE)) {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.SLOW_ADJUSTMENT));
					logger.info("Turn Degrees Slow");
				} else {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
					logger.info("Turn Degrees Fast");
				}
				logger.info("Angle:" + driveTrain.getAngle());
				logger.info("correction:" + correction);
			}
			break;
		case DONE:
			break;
		}

	}
}