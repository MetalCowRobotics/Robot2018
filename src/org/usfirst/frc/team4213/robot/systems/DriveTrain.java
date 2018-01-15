package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain {
	private static final SpeedController LEFT_MOTOR = new Victor(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final SpeedController RIGHT_MOTOR = new Victor(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);
	
	public void setLeftMotorSpeed(double speed){
        System.out.print("LEFT: " + speed);
        LEFT_MOTOR.set(speed);
	}
	
	public void setRightMotorSpeed(double speed){
	    System.out.print("Right: "+ speed);
		RIGHT_MOTOR.set(speed);
	}
	
	public double getRightMotorSpeed(){
		return RIGHT_MOTOR.get();
	}
	
	public double getLeftMotorSpeed(){
		return LEFT_MOTOR.get();
	}

}
