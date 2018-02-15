package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4213.autonomous.LeftSideSwitch;
import org.usfirst.frc.team4213.autonomous.Mission;
import org.usfirst.frc.team4213.autonomous.PassLine;
import org.usfirst.frc.team4213.autonomous.RightSideSwitch;

import org.usfirst.frc.team4213.robot.systems.Climber;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.c
 */
public class Robot extends IterativeRobot {

	private static final Logger logger = Logger.getLogger(Robot.class.getName());
	private static final Level loggingLevel = Level.WARNING;
	// Define Autonomous Missions
	private final String rightSide = "RightSide";
	private final String leftSide = "LeftSide";
	private final String passLine = "PassLine";
	SendableChooser<String> autoChooser = new SendableChooser<>();
	private String autoSelected = passLine;
	private Mission autoMission;

	// PowerDistributionPanel pdp;

	// Systems
	DriverStation driverStation;
	DriveTrain driveTrain;
	Intake intake;
	Elevator elevator;
	Climber climber;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		System.out.println("className:"+Robot.class.getName());
		System.out.println("className using this:" + this.getClass().getName());
		logger.setLevel(loggingLevel);
		logger.entering(this.getClass().getName(), "robotInit");
		logger.log(Level.SEVERE, "Logging Severe Example");
		logger.log(Level.WARNING, "Logging Warning Example");
		logger.log(Level.INFO, "Logging Info Example");
		logger.log(Level.FINE, "Logging Fine Example");
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
		logger.exiting(this.getClass().getName(), "robotInit");
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
		logger.entering(getClass().getName(), "autonomousInit");
		// autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		autoSelected = autoChooser.getSelected();
		if (rightSide == autoSelected) {
			autoMission = new RightSideSwitch();
		} else if (leftSide == autoSelected) {
			autoMission = new LeftSideSwitch();
		} else {
			autoMission = new PassLine();
		}
		logger.log(Level.INFO, "Auto selected: " + autoSelected);
		logger.exiting(this.getClass().getName(), "robotinit");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		logger.entering(this.getClass().getName(), "autonomousPeriodic");
		intake.execute();
		elevator.execute();
		climber.execute();
		autoMission.execute();
		logger.exiting(this.getClass().getName(), "autonomousPeriodic");
	}

	/**
	 * This should be called before teleop for any initilizations
	 */
	@Override
	public void teleopInit() {
		logger.entering(this.getClass().getName(), "teleopInit");
		System.out.println("Teleop Init!");
		logger.exiting(this.getClass().getName(), "teleopInit");
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		logger.entering(this.getClass().getName(), "teleopPeriodic");
		driveTrain.drive();
		elevator.execute();
		intake.execute();
		climber.execute();
		logger.exiting(this.getClass().getName(), "teleopPeriodic");
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testInit() {
		logger.entering(getClass().getName(), "testI");
		elevator.moveElevatortopostion();
		logger.exiting(this.getClass().getName(), "robotinit");
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		logger.entering(this.getClass().getName(), "testP");
		elevator.execute();
		logger.exiting(this.getClass().getName(), "testP");
	}

}
