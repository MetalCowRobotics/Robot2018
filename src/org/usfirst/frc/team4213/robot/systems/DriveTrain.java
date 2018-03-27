package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.lib14.MaxBotixRangeFinder;
import org.usfirst.frc.team4213.lib14.UtilityMethods;
import org.usfirst.frc.team4213.robot.HamburgerDashboard;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain {
	private static final Logger logger = Logger.getLogger(DriveTrain.class.getName());
	private static final DriveTrain instance = new DriveTrain();

	private MasterControls controller = MasterControls.getInstance();

	private static SpeedControllerGroup RightMotor = new SpeedControllerGroup(
			new MCR_SRX(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL1),
			new MCR_SRX(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL2));
	private static SpeedControllerGroup LeftMotor = new SpeedControllerGroup(
			new MCR_SRX(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL1), new MCR_SRX(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL2));

	private static final Encoder rightEncoder = new Encoder(RobotMap.Drivetrain.RIGHT_ENCODER_1,
			RobotMap.Drivetrain.RIGHT_ENCODER_2, false, EncodingType.k4X);
	private static final Encoder leftEncoder = new Encoder(RobotMap.Drivetrain.LEFT_ENCODER_1,
			RobotMap.Drivetrain.LEFT_ENCODER_2, true, EncodingType.k4X);
	private static final DifferentialDrive drive = new DifferentialDrive(LeftMotor, RightMotor);

	private static final ADXRS450_Gyro GYRO = new ADXRS450_Gyro();
	private static BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();

	private MaxBotixRangeFinder wallSensor = new MaxBotixRangeFinder(RobotMap.Drivetrain.RANGE_FINDER);

	public double wallSensorInches() {
		return wallSensor.getDistanceInches() - 11.4;
	}

	private int inverted = 1;

	// Singleton
	protected DriveTrain() {
		logger.setLevel(RobotMap.LogLevels.driveTrainClass);
	}

	public static DriveTrain getInstance() {
		return instance;
	}

	public void drive() {
		printRightEncoder();
		printLeftEncoder();
		double leftSpeed;
		double rightSpeed;
		if (controller.invertDrive()) {
			invert();
		}
		double speed = (controller.forwardSpeed() - controller.reverseSpeed()) * inverted * getThrottle();
		arcadeDrive(speed, UtilityMethods.copySign(controller.direction(), Math.pow(controller.direction(), 2)));
	}

	/**
	 * Used in Autonomous
	 * 
	 * @param speed
	 * @param angle
	 */
	public void arcadeDrive(double speed, double angle) {
		// if only used in autonomous may not need the throttle
		drive.arcadeDrive(speed, angle);
	}

	public void tankDrive() {

	}

	public void devinDrive() {

	}

	public void stop() {
		drive.stopMotor();
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
		if (controller.isCrawlToggle()) {
			return RobotMap.Drivetrain.CRAWL_SPEED;
		} else if (controller.isSprintToggle()) {
			return RobotMap.Drivetrain.SPRINT_SPEED;
		} else {
			return RobotMap.Drivetrain.NORMAL_SPEED;
		}
	}

	private void invert() {
		inverted = inverted * -1;
	}

	private double getLeftEncoderTics() {
		return leftEncoder.getDistance();
	}

	private double getRightEncoderTics() {
		return rightEncoder.getDistance();
	}

	public void printRightEncoder() {
		System.out.println(getRightEncoderTics() + " RightEncoder");
	}

	public void printLeftEncoder() {
		System.out.println(getLeftEncoderTics() + " LeftEncoder");

	}

	public double encoderDifference() {
		return (getRightEncoderTics() - getLeftEncoderTics());
	}

	public double getEncoderTics() {
		//return (getRightEncoderTics() + getLeftEncoderTics()) / 2;
		return getRightEncoderTics();
	}
}
