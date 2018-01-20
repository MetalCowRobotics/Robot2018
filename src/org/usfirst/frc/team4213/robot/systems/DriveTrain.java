package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.DriverController;

import edu.wpi.first.wpilibj.Talon;

public class DriveTrain {
	private static final Talon LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);
	private DriverController controller;
	
	private int inverted = 1;
	
	DriveTrain(DriverController controller){
		this.controller = controller;
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
	
	public void drive (boolean squareUnits) {
		
		if (controller.getAButton()){			//Go into half-arcade
			setLeftMotorSpeed(-controller.getLY() * getThrottle() * inverted);
			setRightMotorSpeed(controller.getLY() * getThrottle() * inverted);
		} else {									//Stay in regular drive
			setLeftMotorSpeed(-controller.getRY() * getThrottle() * inverted);
			setRightMotorSpeed(controller.getLY() * getThrottle() * inverted);
		}
		
	}
	
	/**
	 * Determine the top speed threshold Bumper buttons on the controller will
	 * limit the speed to the CRAWL value Trigger buttons on the controller will
	 * limit the speed to the SPRINT value Otherwise it will allow the robot a
	 * speed up to Normal max.
	 */
	private double getThrottle() {
		if ( controller.getLB() ) {
			return RobotMap.Drivetrain.CRAWL_SPEED;
		} else if ( controller.getRB() ) {
			return RobotMap.Drivetrain.SPRINT_SPEED;
		} else {
			return RobotMap.Drivetrain.NORMAL_SPEED;
		}
	}

}
