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
import org.usfirst.frc.team4213.commands.AngleSwitchMission;
import org.usfirst.frc.team4213.commands.CommandDriveTime;
import org.usfirst.frc.team4213.commands.PassLineMission;
import org.usfirst.frc.team4213.commands.RightSideToLeftSwitchMission;
import org.usfirst.frc.team4213.commands.RightSideToRightSwitchMission;
import org.usfirst.frc.team4213.commands.ScaleEndMission;
import org.usfirst.frc.team4213.commands.ScaleOrSwitchMission;
import org.usfirst.frc.team4213.commands.SwitchEndMission;
import org.usfirst.frc.team4213.commands.SwitchOrScaleMySideMission;
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
	private static final HamburgerDashboard dashboard = HamburgerDashboard.getInstance();
	// Define Autonomous Missions
	final String rightSide = "RightSide";
	final String leftSide = "LeftSide";
	final String passLine = "PassLine";
	final String eitherSide = "eitherSide";
	final String leftSideOfScale = "leftSideOfScale";
	final String rightSideOfScale = "rightSideOfScale";
	final String leftSwitchScale = "LeftSwitchScalePosition";
	final String rightSwitchScale = "RightSwitchScalePosition";
	final String leftScaleSwitch = "LeftScaleOrSwitchPosition";
	final String rightScaleSwitch = "RightScaleOrSwitchPosition";
	final String middlePosition = "MiddlePosition";
	final String angledAutonomous = "AngledAutonomous";
	final String driveTime = "driveTime";
	MCRCommand robotMission; 
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
		autoChooser.addDefault("Pass Line", passLine);
		autoChooser.addObject("Right Side Switch", rightSide);
		autoChooser.addObject("Left Side Switch", leftSide);
		autoChooser.addObject("Switch Either Side", eitherSide);
		autoChooser.addObject("Left Scale", leftSideOfScale);
		autoChooser.addObject("Right Scale", rightSideOfScale);
		autoChooser.addObject("RightSwitchOrScale", rightSwitchScale);
		autoChooser.addObject("LeftSwitchOrScale", leftSwitchScale);
		autoChooser.addObject("RightScaleOrSwitch", rightScaleSwitch);
		autoChooser.addObject("LeftScaleOrSwitch", leftScaleSwitch);
		autoChooser.addObject("MiddlePosition", middlePosition);
		autoChooser.addObject("Angled Autonomous", angledAutonomous);
		autoChooser.addObject("Drive Straight with Time", driveTime);
		 SmartDashboard.putData("Auto choices", autoChooser);
		HamburgerDashboard.getInstance().pushStartPositions();
		HamburgerDashboard.getInstance().pushMCRDriveMode();
		HamburgerDashboard.getInstance().pushElevatorPID();
		HamburgerDashboard.getInstance().pushTurnPID();
		
		// Initialize Robot
		driverStation = DriverStation.getInstance();
		CameraServer.getInstance().startAutomaticCapture(0);

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
		robotMission = buildMission();
		logger.info("Auto selected: " + autoSelected);
		System.out.println(autoSelected);
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
		//autoMission.execute();
		robotMission.run();
		logger.exiting(this.getClass().getName(), "autonomousPeriodic");
		System.out.println();
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
		driveTrain = DriveTrain.getInstance();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		driveTrain.drive();
	}
	
	private MCRCommand buildMission() {
		if (rightSide.equals(autoSelected)) {
			return new RightSideToRightSwitchMission(getNearSwitch());
		} else if (leftSide.equals(autoSelected)) {
			return new SwitchEndMission(Hand.kLeft, getNearSwitch());
		} else if (eitherSide.equals(autoSelected)) {
			if (Hand.kRight.equals(getNearSwitch())) {
				return new RightSideToRightSwitchMission(getNearSwitch());
			} else {
				return new RightSideToLeftSwitchMission(getNearSwitch());
			}
		} else if (leftSideOfScale.equals(autoSelected)) {
			return new ScaleEndMission(Hand.kLeft, getScale());
		} else if (rightSideOfScale.equals(autoSelected)) {
			return new ScaleEndMission(Hand.kRight, getScale());
		} else if (rightSwitchScale.equals(autoSelected)) { 
			return new SwitchOrScaleMySideMission(Hand.kRight, getNearSwitch(), getScale());
		} else if (leftSwitchScale.equals(autoSelected)) { 
			return new SwitchOrScaleMySideMission(Hand.kLeft, getNearSwitch(), getScale());
		} else if (rightScaleSwitch.equals(autoSelected)) { 
			return new ScaleOrSwitchMission(Hand.kRight, getNearSwitch(), getScale());
		} else if (leftScaleSwitch.equals(autoSelected)) { 
			return new ScaleOrSwitchMission(Hand.kLeft, getNearSwitch(), getScale());
		} else if (angledAutonomous.equals(autoSelected)) {
			return new AngleSwitchMission(getNearSwitch());
		} else if (driveTime.equals(autoSelected)) {
			return new CommandDriveTime(5);
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
