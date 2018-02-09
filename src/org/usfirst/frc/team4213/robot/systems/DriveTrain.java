package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MaxBotixRangeFinder;
import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain {
	private static final DriveTrain instance = new DriveTrain();
	private static final Logger logger = Logger.getLogger(DriveTrain.class.getName());

	private MasterControls controller = MasterControls.getInstance();

	private static final Talon LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);
	private static final Encoder rightEncoder = new Encoder(4, 5, false, CounterBase.EncodingType.k4X);
	private static final Encoder leftEncoder = new Encoder(2, 3, true, CounterBase.EncodingType.k4X);
	private static final DifferentialDrive drive = new DifferentialDrive(LEFT_MOTOR, RIGHT_MOTOR);

	private static final ADXRS450_Gyro GYRO = new ADXRS450_Gyro();
	private static BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();

	private MaxBotixRangeFinder wallSensor = new MaxBotixRangeFinder(RobotMap.Drivetrain.RANGE_FINDER);

	public double wallSensorInches() {
		return wallSensor.getDistanceInches() - 11.4;
	}

	private int inverted = 1;

	protected DriveTrain() {
		// Singleton
	}

	public static DriveTrain getInstance() {
		return instance;
	}

	public void drive() {
		if (controller.invertDrive()) {
			invert();
		}

		double leftSpeed = controller.getDriveLeftThrottle() * getThrottle() * inverted;
		double rightSpeed = controller.getDriveRightThrottle() * getThrottle() * inverted;

		if (controller.isHalfArcadeToggle()) { // Go into arcade mode
			drive.arcadeDrive(leftSpeed, rightSpeed, true);
		} else { // Stay in regular Tank drive mode
			drive.tankDrive(leftSpeed, rightSpeed, true);
		}
	}

	public void invert() {
		inverted = inverted * -1;
	}

	public void calibrateGyro() {
		DriverStation.reportWarning("Gyro Reading:" + +GYRO.getAngle(), false);
		DriverStation.reportWarning("Calibrating gyro... ", false);
		GYRO.calibrate();
		DriverStation.reportWarning("... Done! ", false);
		DriverStation.reportWarning("Gryo Reading: " + GYRO.getAngle(), false);
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
		if (controller.isCrawlToggle()) { //
			return RobotMap.Drivetrain.CRAWL_SPEED;
		} else if (controller.isSprintToggle()) { //
			return RobotMap.Drivetrain.SPRINT_SPEED;
		} else { //
			return RobotMap.Drivetrain.NORMAL_SPEED; 
		}
	}

	private void doCheckStyle() { // 1
		int a = 2, b = 3, a1 = 0, b1 = 0, a2 = 0, b2 = 0, c = 0, d = 1, c1 = 1, d1 = 12;
		if (a == b) { // 2
			if (a1 == b1 // 3
					&& c1 == d1) { // 4
				printLeftEncoder();
			} else if (a2 == b2 // 5
					|| c1 < d1) { // 6
				printLeftEncoder();
			} else {
				printLeftEncoder();
			}
		} else if (c == d) { // 7
			while (c == d) { // 8
				printLeftEncoder();
			}
		}  
		// if (34 == seconds && 67 > seconds || (null == rightEncoder && baseSpeed ==
		// 56.7)) {
		// seconds = seconds + 3.5;
		// printLeftEncoder();
		// }
	}

	public Encoder getRightEncoder() {
		return rightEncoder;
	}

	public void printRightEncoder() {
		System.out.println("rightEncoder:" + rightEncoder.getDistance());
	}

	public Encoder getLeftEncoder() {
		return leftEncoder;
	}

	public void printLeftEncoder() {
		System.out.println("leftEncoder:" + leftEncoder.getDistance());
	}

	public double encoderDifference() {
		return (rightEncoder.getDistance() + -leftEncoder.getDistance());
	}

	/**
	 * Used in Autonomous
	 * 
	 * @param speed
	 * @param angle
	 */

	public void arcadeDrive(double speed, double angle) {
		// if only used in autonomous may not need the throttle
		drive.arcadeDrive(speed * getThrottle(), angle);
	}

	public void stop() {
		drive.stopMotor();

	}

	public void setToOpenLoop() {

	}

	public void setToClosedLoop() {

	}

	public double getLeftDistance() {
		return 0;

	}

	public double getLeftSpeed() {
		return 0;

	}

	public double getRightDistance() {
		return 0;

	}

	public double getRightSpeed() {
		return 0;

	}

	public double getEncoderTics() {
		return (rightEncoder.getDistance() + leftEncoder.getDistance()) / 2;
	}
}
