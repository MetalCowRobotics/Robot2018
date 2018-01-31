package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain {
	private static final DriveTrain instance = new DriveTrain();
	private static final Logger logger = Logger.getLogger(DriveTrain.class.getName());

	private MasterControls controller = MasterControls.getInstance();

	private static final Talon LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);
	private static final DifferentialDrive drive = new DifferentialDrive(LEFT_MOTOR, RIGHT_MOTOR);

	private static final ADXRS450_Gyro GYRO = new ADXRS450_Gyro();

	private boolean inverted = false;

	protected DriveTrain() {
		// Singleton
	}

	public static DriveTrain getInstance() {
		// gyroSPI.calibrate();
		// gyroSPI.reset();
		// LEFT_MOTOR.setInverted(true);
		return instance;
	}

	public void drive() {
		if (controller.invertDrive()) {
			invert();
		}

		double leftSpeed = controller.getDriveLeftThrottle() * getThrottle();
		double rightSpeed = controller.getDriveRightThrottle() * getThrottle();

		if (controller.isHalfArcadeToggle()) { // Go into arcade mode
			drive.arcadeDrive(leftSpeed, rightSpeed, true);
		} else { // Stay in regular Tank drive mode
			drive.tankDrive(leftSpeed, rightSpeed, true);
		}
	}

	public void autoDrive(double speed, double angle) {
		drive.arcadeDrive(speed, angle, true);

		// TODO: at some speeds may need to use
		// drive.curvatureDrive(xSpeed, zRotation, isQuickTurn); //for quick turns.

		/**
		 * double angle = gyro.getAngle(); myDrive.arcadeDrive(-1.0, -angle * Kp);
		 */
	}

	public void invert() {
		inverted = !inverted;
	}

	public void calibrateGyro() {
		DriverStation.reportWarning("Calibrating gyro... " + GYRO.getAngle(), false);
		GYRO.calibrate();
		DriverStation.reportWarning("C... Done! " + GYRO.getAngle(), false);
	}

	public void resetGyro() {
		DriverStation.reportWarning("Gyro Before Reset: " + GYRO.getAngle(), false);
		GYRO.reset();
		DriverStation.reportWarning("Gryo After Reset: " + GYRO.getAngle(), false);
	}

	public double getAngle() {
		return GYRO.getAngle();
	}

	/**
	 * Determine the top speed threshold: CRAWL - Lowest speed threshold Normal -
	 * Normal driving conditions SPRINT - Highest speed threshold
	 * 
	 * @link org.usfirst.frc.team4213.robot.RobotMap
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
