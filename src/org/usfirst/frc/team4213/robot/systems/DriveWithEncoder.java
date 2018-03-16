package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.RobotMap;

public class DriveWithEncoder extends AutoDrive {

	private double startTics;
	private double targetInches;
	private double targetTics;

	public DriveWithEncoder(double targetInches) {
		super();
		this.targetInches = targetInches;
		targetTics = (targetInches / RobotMap.DriveWithEncoder.INCHES_PER_ROTATION)
				* RobotMap.DriveWithEncoder.TICS_PER_ROTATION;
		logger.info("target encoder drive:" + targetTics);
	}

	public void run() {
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			startTics = driveTrain.getEncoderTics();
			double setPoint = driveTrain.getAngle();
			driveController = new PDController(setPoint);
			driveTrain.arcadeDrive(RobotMap.DriveWithEncoder.TOP_SPEED, setPoint);
			currentState = State.ACTIVE;
			logger.info("slowdown:" + RobotMap.DriveWithEncoder.SLOW_DOWN_DISTANCE);
			logger.info("targettics:" + targetTics);

			break;
		case ACTIVE:
			double pastTics = driveTrain.getEncoderTics() - startTics;
			System.out.println("THIS IS THE ENCODER TICS: "+driveTrain.getEncoderTics());
			logger.info("pastTics:" + pastTics);
			if (targetTics < pastTics) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				if (targetTics - RobotMap.DriveWithEncoder.SLOW_DOWN_DISTANCE < pastTics) {
					driveTrain.arcadeDrive(RobotMap.DriveWithEncoder.BOTTOM_SPEED,
							limitCorrection(correction, RobotMap.DriveWithEncoder.MAX_ADJUSTMENT));
					logger.info("Drive Inches - slow:");
				} else {
					driveTrain.arcadeDrive(RobotMap.DriveWithEncoder.TOP_SPEED,
							limitCorrection(correction, RobotMap.DriveWithEncoder.MAX_ADJUSTMENT));
					logger.info("Drive inches - fast");
				}
				logger.info("Drive Inches angle: " + driveTrain.getAngle());
			}
			break;
		case DONE:
			break;
		}
	}

}
