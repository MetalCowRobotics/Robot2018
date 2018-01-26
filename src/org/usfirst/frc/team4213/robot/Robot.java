package org.usfirst.frc.team4213.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4213.robot.controllers.DriverController;
import org.usfirst.frc.team4213.robot.systems.Intake;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
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
	boolean firstTime = true;

	SendableChooser<String> autoChooser = new SendableChooser<>();

	// test variable
	long lastTime;

	SendableChooser<String> chooser = new SendableChooser<>();

	// physical components

	// Systems
	DriverController driver;
	Intake intake;

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
		intake = new Intake("I'm not the intake");
		System.out.println("Intake Value:" + intake.getElevator());

		// CameraServer.getInstance().startAutomaticCapture();

		new Thread(() -> {
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			camera.setResolution(640, 480);

			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);

			Mat source = new Mat();
			Mat output = new Mat();

			while (!Thread.interrupted()) {
				cvSink.grabFrame(source);
				Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
				outputStream.putFrame(output);
			}
		}).start();

		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);

		driver = new DriverController(RobotMap.DriverController.USB_PORT);

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

		if (1000 < (System.currentTimeMillis() - lastTime)) {
			lastTime = System.currentTimeMillis();

			SmartDashboard.putNumber("gyroX", accelerometer.getX());
			SmartDashboard.putNumber("gyroY", accelerometer.getY());
			SmartDashboard.putNumber("gyroZ", accelerometer.getZ());

		}
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here

			if (firstTime) {
				firstTime = false;
				System.out.println("customAuto");
			}

			break;
		case defaultAuto:
		default:
			// Put default auto code here

			if (firstTime) {
				firstTime = false;
				System.out.println("defaultAuto");
			}
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

		if (driver.getLB()) {
			intake.powerCubeIntake();
		} else if (driver.getRB()) {
			intake.powerCubeEject();
		} else {
			intake.powerCubeIdle();
		}

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		System.out.println("testing");
	}
}
