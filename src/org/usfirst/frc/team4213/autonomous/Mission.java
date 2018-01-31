package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class Mission {
	
	DriverStation driverStation = DriverStation.getInstance();
	DriveTrain driveTrain = DriveTrain.getInstance();
	
	//state machine steps
	
	//make private step methods one(), two()
	// use state machine to track what step you are on
	
	//by making named missions that extend Mission we should be able to call whatever we need
	
	
	public void execute() {
	
		stepOne();
	}
	
	private void stepOne() {
		double angle = driveTrain.getAngle();
		// double Kp = 0.15;
	
		double Kp = SmartDashboard.getNumber("Kp", .15);
		System.out.println("Drive angle: " + (angle));
		driveTrain.autoDrive(0, -angle * Kp);
	}
	
	private Hand getNearSwitch() {
		if (driverStation.getGameSpecificMessage().toUpperCase().charAt(0) == 'L') {
			return Hand.kLeft;
		}
		return Hand.kRight;
	}

	private Hand getScale() {
		if (driverStation.getGameSpecificMessage().toUpperCase().charAt(1) == 'L') {
			return Hand.kLeft;
		}
		return Hand.kRight;
	}

	private Hand getFarSwitch() {
		if (driverStation.getGameSpecificMessage().toUpperCase().charAt(2) == 'L') {
			return Hand.kLeft;
		}
		return Hand.kRight;
	}
	
	
	
}
