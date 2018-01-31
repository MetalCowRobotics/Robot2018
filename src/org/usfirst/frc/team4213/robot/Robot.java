package org.usfirst.frc.team4213.robot;

import java.util.logging.Level;
import java.util.logging.Logger;

//import org.usfirst.frc.team4213.robot.systems.AutonomousDriveTrain;
import org.usfirst.frc.team4213.robot.systems.Climber;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.c
 */
public class Robot extends IterativeRobot {

	private static final Logger logger = Logger.getLogger(Robot.class.getName());

	// Define Autonomous Missions
	final String defaultAuto = "Default";
	final String customAuto = "Custom";
	SendableChooser<String> autoChooser = new SendableChooser<>();
	String autoSelected = defaultAuto;

	BuiltInAccelerometer accelerometer;
	// PowerDistributionPanel pdp;
	DriverStation driverStation;

	// Systems
	DriveTrain driveTrain;
	Intake intake;
	Elevator elevator;
	Climber climber;
	DifferentialDrive autoDrive;

	ADXRS450_Gyro gyroSPI = new ADXRS450_Gyro();
	// AutonomousDriveTrain autoDriveTrain;

	// Game Variables
	private String gameData;

	// test variable
	long lastTime;
	boolean firstTime = true;

	// Get Scale and Switch information
	public String getGameSpecificMessage() {
		return driverStation.getGameSpecificMessage();
	}

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */

	@Override
	public void robotInit() {
		logger.entering(getClass().getName(), "doIt");
		logger.log(Level.INFO, "Logging Stuff Example");

		// Load available Autonomous missions to the driverstation
		autoSelected = defaultAuto;
		autoChooser.addDefault("Default", defaultAuto);
		autoChooser.addObject("Custom", customAuto);
		SmartDashboard.putData("Auto choices", autoChooser);

		// Initialize Robot
		// pdp = new PowerDistributionPanel();
		driverStation = DriverStation.getInstance();
		accelerometer = new BuiltInAccelerometer();
		// CameraServer.getInstance().startAutomaticCapture();

		// Initialize Systems
		driveTrain = DriveTrain.getInstance();
		elevator = Elevator.getInstance();
		intake = Intake.getInstance();
		climber = Climber.getinstance();
		// autoDriveTrain = AutonomousDriveTrain.getInstance();

		// Initialize Test Variables
		lastTime = System.currentTimeMillis();

		driverStation = DriverStation.getInstance();
		// CameraServer.getInstance().startAutomaticCapture();

		System.out.println("GyroAngle: " + gyroSPI.getAngle() + " | Calibrating Gyro.... ");
		DriverStation.reportWarning("Calibrating gyro... ", false);
		gyroSPI.calibrate();
		DriverStation.reportWarning("C... Done! ", false);
		System.out.println("... Complete!  | New GyroAngle: " + gyroSPI.getAngle());

		logger.exiting(getClass().getName(), "doIt");
		SmartDashboard.putNumber("Kp", .15);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		System.out.println("Autonomous Init!");
		autoSelected = autoChooser.getSelected();
		System.out.println("Auto selected: " + autoSelected);

		// autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		System.out.println("Game Message: " + driverStation.getGameSpecificMessage());
		System.out.println("Gyro Before Reset: " + gyroSPI.getAngle());
		gyroSPI.reset();
		System.out.println("Gryo After Reset: " + gyroSPI.getAngle());
		System.out.println("Autonomous Init - Exit!");
		DriverStation.reportWarning("Robot is Ready!", false);

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		System.out.println("Autonomous Periodic!");

		double angle = gyroSPI.getAngle();
		// double Kp = 0.15;

		double Kp = SmartDashboard.getNumber("Kp", .15);
		System.out.println("Drive angle: " + (angle));
		driveTrain.autoDrive(0, -angle * Kp);

		// switch (autoSelected) {
		// case "ONE":
		// // Put custom auto code here
		//
		// if (firstTime) {
		// firstTime = false;
		// System.out.println("customAuto");
		// }
		//
		// break;
		// case "TWO":
		// default:
		// // Put default auto code here
		//
		// if (firstTime) {
		// firstTime = false;
		// System.out.println("defaultAuto");
		// }
		// break;
		// }
		//

	}

	/**
	 * This should be called before teleop for any initilizations
	 */
	@Override
	public void teleopInit() {
		System.out.println("Teleop Init!");
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		driveTrain.drive();
		elevator.execute();
		intake.execute();
		climber.execute();

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testInit() {

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

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
