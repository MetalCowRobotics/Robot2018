package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class DriveStraightTime extends AutoDrive {
	private Timer timer = new Timer();
	private double seconds = 5;
	
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
			driveTrain.arcadeDrive(RobotMap.DriveStraightTime.TOP_SPEED, setPoint);
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
				driveTrain.arcadeDrive(RobotMap.DriveStraightTime.TOP_SPEED, limitCorrection(correction, RobotMap.DriveStraightTime.MAX_ADJUSTMENT));
				if (seconds - RobotMap.DriveStraightTime.SLOW_DOWN_TIME < timer.get()) {
					driveTrain.arcadeDrive(RobotMap.DriveStraightTime.BOTTOM_SPEED, limitCorrection(correction, RobotMap.DriveStraightTime.MAX_ADJUSTMENT));
					logger.info("Drive Straight Time - slow");
				} else {
					driveTrain.arcadeDrive(RobotMap.DriveStraightTime.TOP_SPEED, limitCorrection(correction, RobotMap.DriveStraightTime.MAX_ADJUSTMENT));
					logger.info("Drive Straight Time - fast");
					logger.info("Angle:" + driveTrain.getAngle());
					logger.info("correction:" + correction);
				}
			}
			break;
		case DONE:
			break;
		}
	}
}

