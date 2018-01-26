package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;
import org.usfirst.frc.team4213.robot.controllers.XboxControllerMetalCow;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Talon;

public class DriveTrain {
	private MasterControls controller;
	
	private static final Talon LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);
	
	private int inverted = 1;
	
	public DriveTrain(MasterControls controller){
		this.controller = controller;
	}
	
	public void drive (boolean squareUnits) {
		double exp = 1.0;
		
		System.out.println("Look I am driving!");
		if(squareUnits) {exp=2.0;}
		
		double leftSpeed = Math.pow( (controller.getDriveLeftThrottle() * getThrottle() * inverted),exp);
		double rightSpeed = Math.pow( (controller.getDriveRightThrottle() * getThrottle() * inverted), exp);
		
		if (controller.isHalfArcadeToggle()){	 //Go into half-arcade
			setLeftMotorSpeed(leftSpeed);
			setRightMotorSpeed(leftSpeed);
		} else {						//Stay in regular drive
			setLeftMotorSpeed(leftSpeed);
			setRightMotorSpeed(rightSpeed);

		}
		
	}
	
	public void invert() {
		inverted *= -1;
	}
	
	private void setLeftMotorSpeed(double speed){
        LEFT_MOTOR.set(speed);
	}
	
	private void setRightMotorSpeed(double speed){
		RIGHT_MOTOR.set(speed);
	}
	
	private double getRightMotorSpeed(){
		return RIGHT_MOTOR.get();
	}
	
	private double getLeftMotorSpeed(){
		return LEFT_MOTOR.get();
	}
	
	
	/**
	 * Determine the top speed threshold Bumper r buttons on the controller will
	 * limit the speed to the SPRINT value Otherbuttons on the controller will
	 * limit the speed to the CRAWL value Triggewise it will allow the robot a
	 * speed up to Normal max.
	 */
	private double getThrottle() {
		if ( controller.isCrawlToggle()) {
			return RobotMap.Drivetrain.CRAWL_SPEED;
		} else if ( controller.isSprintToggle()) {
			return RobotMap.Drivetrain.SPRINT_SPEED;
		} else {
			return RobotMap.Drivetrain.NORMAL_SPEED;
		}
	}

}
