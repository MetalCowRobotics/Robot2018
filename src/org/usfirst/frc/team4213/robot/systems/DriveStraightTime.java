package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;

import edu.wpi.first.wpilibj.Timer;


public class DriveStraightTime extends AutoDrive {
	private Timer timer = new Timer();
	private double seconds = 0;
	private PDController driveController;
	private double baseSpeed = .8;
	private final double maxAdjustment = .4;

	
	public DriveStraightTime(double seconds) {
		super();
		this.seconds = seconds;
	}

	public void run() {
		switch (currentState) {
		case IDLE:
			driveTrain.resetGyro();
			double setPoint = driveTrain.getAngle();
			driveController = new PDController(setPoint);
			driveTrain.arcadeDrive(baseSpeed, setPoint);
			timer.reset();
			timer.start();
			currentState = State.ACTIVE;
			break;
		case ACTIVE:
			if (timer.get() > seconds) {
				timer.stop();
				driveTrain.stop();
				currentState = State.DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
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
