package org.usfirst.frc.team4213.robot;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.usfirst.frc.team4213.autonomous.AutoMission1;
import org.usfirst.frc.team4213.autonomous.Mission;
import org.usfirst.frc.team4213.autonomous.PassLine;
import org.usfirst.frc.team4213.autonomous.RightSideSwitch;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
//import org.usfirst.frc.team4213.robot.systems.AutonomousDriveTrain;
import org.usfirst.frc.team4213.robot.systems.Climber;
import org.usfirst.frc.team4213.robot.systems.DriveStraightTime;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.DriverStation;
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
	final String rightSide = "RightSide";
	final String leftSide = "LeftSide";
	final String passLine = "PassLine";
	SendableChooser<String> autoChooser = new SendableChooser<>();
	String autoSelected = passLine;

	Mission autoMission;
	AutoDrive driveStraight;
	TurnDegrees turnDegrees;
	// PowerDistributionPanel pdp;
	DriverStation driverStation;
	DriveWithEncoder driveWithEncoder;

	// Systems
	DriveTrain driveTrain;
	Intake intake;
	Elevator elevator;
	Climber climber;
	DifferentialDrive autoDrive;

	// temp variables
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
		System.out.println("ClassName:" + Robot.class.getName());
		System.out.println("ClassName2:" + this.getClass().getName());
		logger.log(Level.INFO, "Logging Stuff Example");

		// Load available Autonomous missions to the driverstation
		autoSelected = rightSide;
		autoChooser.addObject("RightSideSwitch", rightSide);
		autoChooser.addObject("LeftSideSwitch", leftSide);
		autoChooser.addDefault("PassLine", passLine);
		SmartDashboard.putData("Auto choices", autoChooser);

		// Initialize Robot
		// pdp = new PowerDistributionPanel();
		driverStation = DriverStation.getInstance();

		// CameraServer.getInstance().startAutomaticCapture();

		// Initialize Systems
		driveTrain = DriveTrain.getInstance();
		elevator = Elevator.getInstance();
		intake = Intake.getInstance();
		climber = Climber.getinstance();

		driveTrain.calibrateGyro();

		DriverStation.reportWarning("ROBOT SETUP COMPLETE!  Ready to Rumble!", false);

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
		logger.entering(getClass().getName(), "doIt");
		logger.entering("autonomousInit", "");

		driveTrain.resetGyro();

		// TODO: Choose autonomous mission here?
		
		
		// autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		autoSelected = autoChooser.getSelected();
		if(rightSide == autoSelected) {
			autoMission = new RightSideSwitch();
		} else if(leftSide == autoSelected) {
			autoMission = new AutoMission1();
		} else {
			autoMission = new PassLine();
		}
		System.out.println("Auto selected: " + autoSelected);
		System.out.println("Auto selected: " + autoSelected);
		// switch (autoSelected) {
		// case "ONE":
		// // Put custom auto code here
		// autoMission = new AutoMission1();
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
		// autoMission = new AutoMission1();
		System.out.println("Autonomous Init - Exit!");
		logger.exiting(getClass().getName(), "doIt");
		firstTime = true;
		driveStraight = new DriveStraightTime(5);
		turnDegrees = new TurnDegrees(90);
		driveWithEncoder = new DriveWithEncoder(48);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		autoMission.execute();
		intake.execute();
		elevator.execute();
		climber.execute();
		
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
		// DriveTrain.printRightEncoder();
		// DriveTrain.printLeftEncoder;
		driveWithEncoder = new DriveWithEncoder(10);

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		if (!driveWithEncoder.isFinished()) {
			driveWithEncoder.run();
		}

	}

}
