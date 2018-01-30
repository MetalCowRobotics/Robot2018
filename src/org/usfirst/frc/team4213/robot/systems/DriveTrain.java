package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Talon;

public class DriveTrain {
	private MasterControls controller;

	private static final Talon LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);
	private static final ADXRS450_Gyro gyroSPI = new ADXRS450_Gyro();
	// gyroSPI = new ADXRS453Gyro();
	// MY_GYRO = new AnalogGyro(RobotMap.Drivetrain.MY_GYRO_CHANNEL);

	private int inverted = 1;

	public DriveTrain(MasterControls controller) {
		this.controller = controller;
		gyroSPI.calibrate();
		gyroSPI.reset();
		LEFT_MOTOR.setInverted(true);
	}

	public void drive() {

		if (controller.invertDrive()) {
			invert();
		}
		// TODO: fix exponetion
		double leftSpeed = squareSpeed(controller.getDriveLeftThrottle());
		double rightSpeed = squareSpeed(controller.getDriveRightThrottle());

		if (controller.isHalfArcadeToggle()) { // Go into half-arcade
			setLeftMotorSpeed(leftSpeed);
			setRightMotorSpeed(leftSpeed);
		} else { // Stay in regular drive
			setLeftMotorSpeed(leftSpeed);
			setRightMotorSpeed(rightSpeed);

		}

		System.out.println("angle:" + gyroSPI.getAngle());

	}

	private double squareSpeed(double controllerSpeed) {
		if (0 < controllerSpeed) {
			return Math.pow(controller.getDriveLeftThrottle(), 2);
		} else {
			return -1 * Math.pow(controller.getDriveLeftThrottle(), 2);
		}
	}

	public void invert() {
		inverted *= -1;
	}

	private void setLeftMotorSpeed(double speed) {
		LEFT_MOTOR.set(speed * getThrottle() * inverted);
	}

	private void setRightMotorSpeed(double speed) {
		RIGHT_MOTOR.set(speed * getThrottle() * inverted);
	}

	private double getRightMotorSpeed() {
		return RIGHT_MOTOR.get();
	}

	private double getLeftMotorSpeed() {
		return LEFT_MOTOR.get();
	}

	/**
	 * Determine the top speed threshold Bumper r buttons on the controller will
	 * limit the speed to the SPRINT value Otherbuttons on the controller will limit
	 * the speed to the CRAWL value Triggewise it will allow the robot a speed up to
	 * Normal max.
	 */
	private double getThrottle() {
		if (controller.isCrawlToggle()) {
			return RobotMap.Drivetrain.CRAWL_SPEED;
		} else if (controller.isSprintToggle()) {
			return RobotMap.Drivetrain.SPRINT_SPEED;
		} else {
			return RobotMap.Drivetrain.NORMAL_SPEED;
		}
	}

}
