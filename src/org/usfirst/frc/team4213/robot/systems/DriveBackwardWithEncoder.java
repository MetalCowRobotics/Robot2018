package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.RobotMap;
//import org.usfirst.frc.team4213.robot.systems.AutoDrive.State;

public class DriveBackwardWithEncoder extends AutoDrive {
	
	private double startTics;
	private double targetInches;
	private double targetTics;

	public DriveBackwardWithEncoder(double targetInches) {
		super();
		this.targetInches = targetInches;
		targetTics = (targetInches / RobotMap.DriveWithEncoder.INCHES_PER_ROTATION)
				* RobotMap.DriveWithEncoder.TICS_PER_ROTATION;
		System.out.println("THIS IS THE TARGET TICS" + targetTics);
	}

	public void run() {
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			startTics = driveTrain.getEncoderTics();
			double setPoint = driveTrain.getAngle();
			driveController = new PDController(setPoint);
			driveTrain.arcadeDrive(-RobotMap.DriveWithEncoder.TOP_SPEED, setPoint);
			currentState = State.ACTIVE;
			System.out.println("slowdown:" + RobotMap.DriveWithEncoder.SLOW_DOWN_DISTANCE);
			System.out.println("targettics:" + targetTics);

			break;
		case ACTIVE:
			double pastTics = driveTrain.getEncoderTics() - startTics;
			System.out.println("pastTics:" + pastTics);
			if (targetTics > pastTics) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				if (targetTics + RobotMap.DriveWithEncoder.SLOW_DOWN_DISTANCE > pastTics) {
					driveTrain.arcadeDrive(-RobotMap.DriveWithEncoder.BOTTOM_SPEED,
							limitCorrection(correction, RobotMap.DriveWithEncoder.MAX_ADJUSTMENT));
					System.out.println("slow:");
				} else {
					driveTrain.arcadeDrive(-RobotMap.DriveWithEncoder.TOP_SPEED,
							limitCorrection(correction, RobotMap.DriveWithEncoder.MAX_ADJUSTMENT));
					System.out.println("fast");

				}
				System.out.println("angle: " + driveTrain.getAngle());
			}
			break;
		case DONE:
			break;
		}
	}

}
