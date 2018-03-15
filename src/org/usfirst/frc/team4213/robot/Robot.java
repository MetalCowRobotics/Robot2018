package org.usfirst.frc.team4213.robot;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.autonomous.LeftPosLeftSwitchEnd;
import org.usfirst.frc.team4213.autonomous.LeftPosition;
import org.usfirst.frc.team4213.autonomous.LeftSideScale;
import org.usfirst.frc.team4213.autonomous.Mission;
import org.usfirst.frc.team4213.autonomous.PassLine;
import org.usfirst.frc.team4213.autonomous.RightPosToRigthSwitch;
import org.usfirst.frc.team4213.autonomous.RightPosToSwitchEitherSide;
import org.usfirst.frc.team4213.autonomous.RightPosition;
import org.usfirst.frc.team4213.robot.HamburgerDashboard.AutoMissionOptions;
import org.usfirst.frc.team4213.robot.HamburgerDashboard.StartPosition;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.Climber;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import org.usfirst.frc.team4213.robot.systems.AutonomousDriveTrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.c
 */
public class Robot extends IterativeRobot {
	private static final Logger logger = Logger.getLogger(Robot.class.getName());
	private static final HamburgerDashboard dashboard = HamburgerDashboard.getInstance();
	// Define Autonomous Missions
	final String rightSide = "RightSide";
	final String leftSide = "LeftSide";
	final String passLine = "PassLine";
	final String eitherSide = "eitherSide";
	final String leftSideOfScale = "leftSideOfScale";
	final String leftPosition = "LeftPosition";
	final String rightPosition = "RightPosition";
	final String middlePosition = "MiddlePosition";
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

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		logger.setLevel(RobotMap.LogLevels.robotClass);
		logger.entering(this.getClass().getName(), "robotInit");
		//setup the smartdashboard
		HamburgerDashboard.getInstance().initializeDashboard();
//		HamburgerDashboard.getInstance().pushAutonomousMissions();
		 autoChooser.addObject("Right Side Switch", rightSide);
		 autoChooser.addObject("Left Side Switch", leftSide);
		 autoChooser.addDefault("Pass Line", passLine);
		 autoChooser.addObject("Switch Either Side", eitherSide);
		 autoChooser.addObject("Left Scale", leftSideOfScale );
		 autoChooser.addObject("RightPosition", rightPosition);
		 autoChooser.addObject("LeftPosition", leftPosition);
		 autoChooser.addObject("MiddlePosition", middlePosition);
		 SmartDashboard.putData("Auto choices", autoChooser);
		HamburgerDashboard.getInstance().pushStartPositions();
		HamburgerDashboard.getInstance().pushMCRDriveMode();
		HamburgerDashboard.getInstance().pushElevatorPID();
		
		// Initialize Robot
		//driverStation = DriverStation.getInstance();
		CameraServer.getInstance().startAutomaticCapture();

		// Initialize Systems
		driveTrain = DriveTrain.getInstance();
		elevator = Elevator.getInstance();
		intake = Intake.getInstance();
		climber = Climber.getinstance();
		//calibrate Gyro
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
		logger.entering("autonomousInit", "");
		//StartPosition position = dashboard.getStartPosition();
		//autoMission = selectMission(dashboard.getAutoMision());
		//autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		autoSelected = autoChooser.getSelected();
		if (rightSide == autoSelected) {
			autoMission = new RightPosToRigthSwitch();
		} else if (leftSide == autoSelected) {
			autoMission = new LeftPosLeftSwitchEnd();
		} else if (eitherSide == autoSelected) {
			autoMission = new RightPosToSwitchEitherSide();
		} else if (leftSideOfScale == autoSelected) {
			autoMission = new LeftSideScale();
		} else if (passLine == autoSelected) {
			autoMission = new PassLine();
		} else if (rightPosition == autoSelected) {
			autoMission = new RightPosition();
		} else if (leftPosition == autoSelected) {
			autoMission = new LeftPosition();
		} else {
			autoMission = new PassLine();
		}
		logger.info("Auto selected: " + autoSelected);
		logger.exiting(getClass().getName(), "doIt");
	}

	private Mission selectMission(AutoMissionOptions chosenAuto) {
		switch (chosenAuto) {
		case rightSwitch:
			return new RightPosToRigthSwitch();
		case leftSwitch:
			return new LeftPosLeftSwitchEnd();
		case switchMySide:
			return null;
		case switchEitherSide:
			return new RightPosToSwitchEitherSide();
		case scaleMySide:
			return null;
		case leftScale:
			return null;
		case rightScale:
			return new RightPosition();
		case scaleOrSwitch:
			return new LeftPosition();
		case switchOrScale:
			return null;
		default:
			return new PassLine();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		logger.entering(this.getClass().getName(), "autonomousPeriodic");
		intake.execute();
		elevator.execute();
		autoMission.execute();
		logger.exiting(this.getClass().getName(), "autonomousPeriodic");
	}

	/**
	 * This should be called before teleop for any initilizations
	 */
	@Override
	public void teleopInit() {
		logger.info("Teleop Init!");
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

}
