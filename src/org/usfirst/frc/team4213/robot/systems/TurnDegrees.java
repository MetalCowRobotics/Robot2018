package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.lib14.UtilityMethods;

public class TurnDegrees extends AutoDrive {
	private double baseSpeed = 0;
	private double degrees;
	private final double maxAdjustment = .5;
	private final double variance = .25;
	private double slowAdjustment = .2;
	private double slowVariance = 5;
	
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
			driveTrain.arcadeDrive(baseSpeed, driveController.calculateAdjustment(setPoint));
			currentState = State.ACTIVE;
			break;
		case ACTIVE:
			double currentAngle = driveTrain.getAngle();
			if (UtilityMethods.between(currentAngle, degrees - variance, degrees + variance)) {
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
				driveTrain.arcadeDrive(baseSpeed, limitCorrection(correction, maxAdjustment));
				if (UtilityMethods.between(currentAngle, degrees - slowVariance, degrees + slowVariance)) {
					driveTrain.arcadeDrive(baseSpeed, limitCorrection(correction, slowAdjustment));
					System.out.println("slow");
				} else {
					driveTrain.arcadeDrive(baseSpeed, limitCorrection(correction, maxAdjustment));
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