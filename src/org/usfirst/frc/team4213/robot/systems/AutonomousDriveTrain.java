package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Talon;

public class AutonomousDriveTrain {
	private static final AutonomousDriveTrain instance = new AutonomousDriveTrain();
	private static final Logger logger = Logger.getLogger(DriveTrain.class.getName());
	
	private static final Talon LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);

	private static final DifferentialDrive drive = new DifferentialDrive(LEFT_MOTOR, RIGHT_MOTOR);
	
	private AutonomousDriveTrain() {
		// Singleton
	}

	public static AutonomousDriveTrain getInstance() {
		
		return instance;
	}
	
	public void drive(double speed, double angle) {
		drive.arcadeDrive(speed, angle, true);
		
		//TODO: at some speeds may need to use
		//drive.curvatureDrive(xSpeed, zRotation, isQuickTurn); //for quick turns.

		/**
   		double angle = gyro.getAngle();
    		myDrive.arcadeDrive(-1.0, -angle * Kp);
		 */
		
	}

}
