package org.usfirst.frc.team4213.robot;

import org.usfirst.frc.team4213.robot.controllers.MasterControls;
import org.usfirst.frc.team4213.robot.controllers.XboxControllerMetalCow;
import org.usfirst.frc.team4213.robot.systems.Climber;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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

	// Define Autonomous Missions
	final String defaultAuto = "Default";
	final String customAuto = "Custom";
	SendableChooser<String> autoChooser = new SendableChooser<>();
	String autoSelected = defaultAuto;

	BuiltInAccelerometer accelerometer;
	//PowerDistributionPanel pdp;
	DriverStation driverStation;

	// Systems
	DriveTrain driveTrain;
	MasterControls controls;
	Intake intake;
	Elevator elevator;
	Climber climber;

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
		// Load available Autonomous misisons to the driverstation
		autoSelected = defaultAuto;
		autoChooser.addDefault("Default", defaultAuto);
		autoChooser.addObject("Custom", customAuto);
		SmartDashboard.putData("Auto choices", autoChooser);

		// Initialize Robot
		//pdp = new PowerDistributionPanel();
		driverStation = DriverStation.getInstance();
		accelerometer = new BuiltInAccelerometer();
		//CameraServer.getInstance().startAutomaticCapture();

		// Intitialize Systems
		controls = new MasterControls(new XboxControllerMetalCow(RobotMap.DriverController.USB_PORT),
				new XboxControllerMetalCow(RobotMap.OperatorController.USB_PORT));
		driveTrain = new DriveTrain(controls);
		elevator = new Elevator(controls);
		intake = new Intake(controls, elevator);
		climber = new Climber(controls);

		// Initialize Test Variables
		lastTime = System.currentTimeMillis();

		driverStation = DriverStation.getInstance();
		//CameraServer.getInstance().startAutomaticCapture();

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

		while (null == gameData) {
			gameData = getGameSpecificMessage();
		}

		// autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		System.out.println("Auto selected: " + autoSelected);

		System.out.println(driverStation.getGameSpecificMessage());

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {

		switch (autoSelected) {
		case "ONE":
			// Put custom auto code here

			if (firstTime) {
				firstTime = false;
				System.out.println("customAuto");
			}

			break;
		case "TWO":
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
