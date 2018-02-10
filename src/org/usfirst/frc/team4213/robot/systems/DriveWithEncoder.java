package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.RobotMap;

public class DriveWithEncoder extends AutoDrive {
	private double baseSpeed = .6;
	private double slowSpeed = .4;
	private final double maxAdjustment = .4;
	private int ticsPerRotation = 354;
	private double inchesPerRotation = Math.PI * RobotMap.Drivetrain.WHEEL_DIAMETER;
	private double startTics = driveTrain.getEncoderTics();
	private double targetInches;
	private double targetTics;
	private double slowdownDistance = (12 / inchesPerRotation) * ticsPerRotation;

	public DriveWithEncoder(double targetInches) {
		super();
		this.targetInches = targetInches;
		targetTics = targetInches / inchesPerRotation * ticsPerRotation;
	}

	public void run() {
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			double setPoint = driveTrain.getAngle();
			driveController = new PDController(setPoint);
			driveTrain.arcadeDrive(baseSpeed, setPoint);
			currentState = State.ACTIVE;
			System.out.println("slowdown:" + slowdownDistance);
			System.out.println("targettics:" + targetTics);

			break;
		case ACTIVE:
			double pastTics = driveTrain.getEncoderTics() - startTics;
			System.out.println("pastTics:" + pastTics);
			if (targetTics < pastTics) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				if (targetTics - slowdownDistance < pastTics) {
					driveTrain.arcadeDrive(slowSpeed, limitCorrection(correction, maxAdjustment));
					System.out.println("slow:" );
				} else {
					driveTrain.arcadeDrive(baseSpeed, limitCorrection(correction, maxAdjustment));
					System.out.println("fast" );

				}
				System.out.println("angle: " + driveTrain.getAngle());
			}
			break;
		case DONE:
			break;
		}
	}

}
