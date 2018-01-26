package org.usfirst.frc.team4213.robot;

import org.usfirst.frc.team4213.robot.controllers.MasterControls;
import org.usfirst.frc.team4213.robot.controllers.XboxControllerMetalCow;
import org.usfirst.frc.team4213.robot.systems.Climber;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
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

	// Systems
	MasterControls controls;
	DriveTrain driveTrain;
	Intake intake;
	Elevator elevator;
	Climber climber;
	
	//Other Stuff
	DriverStation driverStation;
	SendableChooser<String> autoChooser = new SendableChooser<>();
	private enum AutoModes { One, Two, Three, Four }; //TODO: Name these something later
	//final String defaultAuto = "Default";
	//final String customAuto = "Custom";
	AutoModes autoSelected;
	boolean firstTime = true;
	

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */

	@Override
	public void robotInit() {
		
		controls = new MasterControls(
					new XboxControllerMetalCow(RobotMap.DriverController.USB_PORT),
					new XboxControllerMetalCow(RobotMap.OperatorController.USB_PORT)
				);
		driveTrain = new DriveTrain(controls);
		elevator = new Elevator(controls);
		intake = new Intake(controls, elevator);
		climber = new Climber(controls);

		driverStation = DriverStation.getInstance();

		autoSelected = AutoModes.One;
		autoChooser.addDefault("Default", defaultAuto);
		autoChooser.addObject("Custom", customAuto);
		SmartDashboard.putData("Auto choices", autoChooser);

		CameraServer.getInstance().startAutomaticCapture();

		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		

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
		// autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {

		switch (autoSelected) {
		case AutoOne:
			// Put custom auto code here

			if (firstTime) {
				firstTime = false;
				System.out.println("customAuto");
			}

			break;
		case AutoTwo:
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
		driveTrain.drive(true);
		

		System.out.println(pdp.getTemperature() + " Degrees Celcius");
		System.out.println(pdp.getCurrent(1) + " Amps");
		System.out.println(getGameSpecficMessage());
		System.out.println(driverStation.getGameSpecificMessage());

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		System.out.println("testing");
	}
}
