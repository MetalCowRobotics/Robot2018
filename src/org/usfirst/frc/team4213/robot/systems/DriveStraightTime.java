package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;

import edu.wpi.first.wpilibj.Timer;

public class DriveStraightTime extends AutoDrive {
	private Timer timer = new Timer();
	private double seconds = 5;
	private double baseSpeed = .6;
	private final double maxAdjustment = .4;
	private double slowSpeed = .3;
	private double slowdownTime = 2;
	
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
				if (seconds - slowdownTime < timer.get()) {
					driveTrain.arcadeDrive(slowSpeed, limitCorrection(correction, maxAdjustment));
					System.out.println("slow");
				} else {
					driveTrain.arcadeDrive(baseSpeed, limitCorrection(correction, maxAdjustment));
					System.out.println("fast");
				System.out.println("Angle:" + driveTrain.getAngle());
				System.out.println("correction:" + correction);
				}
			}
			break;
		case DONE:
			break;
		}
	}

}

