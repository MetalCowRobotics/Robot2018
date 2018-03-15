package org.usfirst.frc.team4213.robot;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.usfirst.frc.team4213.autonomous.AngleSwitchMission;
import org.usfirst.frc.team4213.autonomous.AngledAutonomous;
import org.usfirst.frc.team4213.autonomous.LeftPosition;
import org.usfirst.frc.team4213.autonomous.LeftSideScale;
import org.usfirst.frc.team4213.autonomous.LeftSideSwitch;
import org.usfirst.frc.team4213.autonomous.Mission;
import org.usfirst.frc.team4213.autonomous.PassLine;
import org.usfirst.frc.team4213.autonomous.PassLineMission;
import org.usfirst.frc.team4213.autonomous.RightPosToSwitchEitherSide;
import org.usfirst.frc.team4213.autonomous.RightPosition;
import org.usfirst.frc.team4213.autonomous.RightSideSwitch;
import org.usfirst.frc.team4213.autonomous.RightSideToLeftSwitchMission;
import org.usfirst.frc.team4213.autonomous.RightSideToRightSwitchMission;
import org.usfirst.frc.team4213.autonomous.ScaleEndMission;
import org.usfirst.frc.team4213.autonomous.SwitchEndMission;
import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.Climber;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
	private static final Level loggingLevel = Level.WARNING;
	// Define Autonomous Missions
	final String rightSide = "RightSide";
	final String leftSide = "LeftSide";
	final String passLine = "PassLine";
	final String eitherSide = "eitherSide";
	final String leftSideOfScale = "leftSideOfScale";
	final String leftPosition = "LeftPosition";
	final String rightPosition = "RightPosition";
	final String middlePosition = "MiddlePosition";
	final String angledAutonomous = "AngledAutonomous";
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
		logger.setLevel(loggingLevel);
		logger.entering(this.getClass().getName(), "robotInit");
		// setup the smartdashboard
		autoChooser.addObject("Right Side Switch", rightSide);
		autoChooser.addObject("Left Side Switch", leftSide);
		autoChooser.addDefault("Pass Line", passLine);
		autoChooser.addObject("Switch Either Side", eitherSide);
		autoChooser.addObject("Left Scale", leftSideOfScale);
		autoChooser.addObject("RightPosition", rightPosition);
		autoChooser.addObject("LeftPosition", leftPosition);
		autoChooser.addObject("MiddlePosition", middlePosition);
		autoChooser.addObject("Angled Autonomous", angledAutonomous);
		SmartDashboard.putData(autoChooser);
		HamburgerDashboard.getInstance().initializeDashboard();
		HamburgerDashboard.getInstance().pushAutonomousMissions();
		HamburgerDashboard.getInstance().pushStartPositions();
		HamburgerDashboard.getInstance().pushDevinDrive();
		HamburgerDashboard.getInstance().pushElevatorPID();
		HamburgerDashboard.getInstance().pushTurnPID();

		// Initialize Robot
		driverStation = DriverStation.getInstance();
		CameraServer.getInstance().startAutomaticCapture();

		// Initialize Systems
		driveTrain = DriveTrain.getInstance();
		elevator = Elevator.getInstance();
		intake = Intake.getInstance();
		climber = Climber.getinstance();
		// calibrate Gyro
		driveTrain.calibrateGyro();
		DriverStation.reportWarning("ROBOT SETUP COMPLETE!  Ready to Rumble!", false);
	}
	
	private MCRCommand robotMission;

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
		// AutoMission mission = HamburgerDashboard.getInstance().getAutoMision();
		// StartPosition position = HamburgerDashboard.getInstance().getStartPosition();
		// autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);

		autoSelected = autoChooser.getSelected();
		System.out.println("Auto Selected:" + autoSelected);
		if (rightSide == autoSelected) {
			autoMission = new RightSideSwitch();
		} else if (leftSide == autoSelected) {
			autoMission = new LeftSideSwitch();
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
		} else if (angledAutonomous == autoSelected) {
			autoMission = new AngledAutonomous();
			logger.info("in angle auto");
		}

		robotMission = buildMission();
		System.out.println("Auto selected: " + autoSelected);
		logger.exiting(getClass().getName(), "doIt");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		logger.entering(this.getClass().getName(), "autonomousPeriodic");
		intake.execute();
		elevator.execute();
//		autoMission.execute();
		robotMission.run();
		logger.exiting(this.getClass().getName(), "autonomousPeriodic");
	}

	/**
	 * This should be called before teleop for any initilizations
	 */
	@Override
	public void teleopInit() {
		System.out.println("Teleop Init!");
		// elevator.setPosition(6);
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

	// DigitalInput ElevatorUp, ElevatorDown, IntakeUp, IntakeDown;

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testInit() {

	}

	private boolean firstTime = true;

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		// System.out.println(driveTrain.wallSensorInches());
		elevator.execute();

		if (firstTime) {
			// elevator.hold(1000);
			// elevator.moveElevatorToPosition(RobotMap.Elevator.EXCHANGE_HEIGHT);
			firstTime = false;
		}
	}

	private MCRCommand buildMission() {
		if (rightSide == autoSelected) {
			return new RightSideToRightSwitchMission(getNearSwitch());
		} else if (leftSide == autoSelected) {
			return new SwitchEndMission(Hand.kLeft, getNearSwitch());
		} else if (eitherSide == autoSelected) {
			if (Hand.kRight.equals(getNearSwitch())) {
				return new RightSideToRightSwitchMission(getNearSwitch());
			} else {
				return new RightSideToLeftSwitchMission(getNearSwitch());
			}
		} else if (leftSideOfScale == autoSelected) {
			return new ScaleEndMission(Hand.kLeft, getScale());
		} else if (rightPosition == autoSelected) { // not sure what this is
			return new ScaleEndMission(Hand.kRight, getScale());
		} else if (angledAutonomous == autoSelected) {
			return new AngleSwitchMission(getNearSwitch());
		} else {
			return new PassLineMission();
		}
	}

	private Hand getNearSwitch() {
		return driverStation.getGameSpecificMessage().toUpperCase().charAt(0) == 'L' ? Hand.kLeft : Hand.kRight;
	}

	private Hand getScale() {
		return driverStation.getGameSpecificMessage().toUpperCase().charAt(1) == 'L' ? Hand.kLeft : Hand.kRight;
	}

}
