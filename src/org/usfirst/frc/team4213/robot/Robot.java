package org.usfirst.frc.team4213.robot;

import org.usfirst.frc.team4213.robot.controllers.DriverController;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	final String defaultAuto = "Default";
	final String customAuto = "Custom";
	BuiltInAccelerometer accelerometer;
	String autoSelected;

	SendableChooser<String> autoChooser = new SendableChooser<>();

	// test variable
	long lastTime;

	SendableChooser<String> chooser = new SendableChooser<>();

	DriverController driver;
	SpeedController leftMotor;
	SpeedController rightMotor;
	Accelerometer acc;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */

	@Override
	public void robotInit() {
		autoSelected = defaultAuto;
		autoChooser.addDefault("Default", defaultAuto);
		autoChooser.addObject("Custom", customAuto);
		SmartDashboard.putData("Auto choices", autoChooser);

		driver = new DriverController(RobotMap.DriverController.USB_PORT);
		accelerometer = new BuiltInAccelerometer();

		lastTime = System.currentTimeMillis();

		driver = new DriverController(RobotMap.DriverController.USB_PORT);
		leftMotor = new Talon(0);
		rightMotor = new Talon(1);
		acc = new BuiltInAccelerometer();
		
		CameraServer.getInstance().startAutomaticCapture(); 

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
		autoSelected = autoChooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if (3000 < (System.currentTimeMillis() - lastTime)) {
			lastTime = System.currentTimeMillis();

			SmartDashboard.putNumber("gyroX", accelerometer.getX());
			SmartDashboard.putNumber("gyroY", accelerometer.getY());
			SmartDashboard.putNumber("gyroZ", accelerometer.getZ());

		}
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			System.out.println("customAuto");

			break;
		case defaultAuto:
		default:
			// Put default auto code here
			System.out.println("defaultAuto");
			break;
		}
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

		// if you run this the Left Joystick should make the

		driver.rumbleAll(acc.getX());
		System.out.println(acc.getX());

		// Log something to the Driverstation

		leftMotor.set(driver.getLY());
		rightMotor.set(-driver.getRY());

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		System.out.println("testing");
	}
}
