package org.usfirst.frc.team4213.robot;

import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.robot.systems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HamburgerDashboard {
	private static org.usfirst.frc.team4213.robot.HamburgerDashboard ourInstance = new org.usfirst.frc.team4213.robot.HamburgerDashboard();

	public static org.usfirst.frc.team4213.robot.HamburgerDashboard getInstance() {
		return ourInstance;
	}

	private HamburgerDashboard() {
	}

	public enum AutoMission {
		passLine, rightSwitch, leftSwitch, switchMySide, switchEitherSide, scaleMySide, leftScale, rightScale, scaleOrSwitch, switchOrScale
	};

	public enum StartPosition {
		left, right, farRight
	};

	// Define Autonomous Missions //remove if enum works
	private final String rightSide = "RightSide";
	private final String leftSide = "LeftSide";
	private final String passLine = "PassLine";
	private final String eitherSide = "eitherSide";
	private final String leftSideOfScale = "leftSideOfScale";
	private final String leftPosition = "LeftPosition";
	private final String rightPosition = "RightPosition";
	private final String middlePosition = "MiddlePosition";

	private SendableChooser<String> autoChooser = new SendableChooser<>();
	private SendableChooser<String> positionChooser = new SendableChooser<>();
	private AutoMission mission = AutoMission.passLine;;
	private StartPosition position = StartPosition.right;
	private String autoSelected = passLine; // remove if enum works

	private PowerDistributionPanel pdp;
	private DriverStation driverStation;

	public void initializeDashboard() {
		pdp = new PowerDistributionPanel();
		driverStation = edu.wpi.first.wpilibj.DriverStation.getInstance();
		// CameraServer.getInstance().startAutomaticCapture();
		SmartDashboard.putBoolean("Auto Position For Second Cube", RobotMap.Autonomous.SecondaryCube);
	}

	public void pushAutonomousMissions() {
		// Load available Autonomous missions to the driverstation
		autoSelected = rightSide;
		 autoChooser.addObject("Right Side Switch", rightSide);
		 autoChooser.addObject("Left Side Switch", leftSide);
		 autoChooser.addDefault("Pass Line", passLine);
		 autoChooser.addObject("Switch Either Side", eitherSide);
		 autoChooser.addObject("Left Scale", leftSideOfScale );
		 autoChooser.addObject("RightPosition", rightPosition);
		 autoChooser.addObject("LeftPosition", leftPosition);
		 autoChooser.addObject("MiddlePosition", middlePosition);
//		autoChooser.addDefault("Pass Line", AutoMission.passLine.name());
//		autoChooser.addObject("Right Side Switch", AutoMission.rightSwitch.name());
//		autoChooser.addObject("Left Side Switch", AutoMission.leftSwitch.name());
//		autoChooser.addObject("Switch Either Side", AutoMission.switchEitherSide.name());
//		autoChooser.addObject("Left Scale", AutoMission.leftScale.name());
//		autoChooser.addObject("Right Scale", AutoMission.rightScale.name());
//		autoChooser.addObject("Scale or Switch My Side", AutoMission.scaleOrSwitch.name());
//		autoChooser.addObject("Switch or Scale My Side", AutoMission.switchOrScale.name());
		SmartDashboard.putData("Auto choices", autoChooser);
	}

	public void pushStartPositions() {
		positionChooser.addObject("Left of Switch", StartPosition.left.name());
		positionChooser.addObject("Right side in front of Switch", StartPosition.right.name());
		positionChooser.addObject("Right of Switch", StartPosition.farRight.name());
		SmartDashboard.putData("Starting Position", positionChooser);
	}

	public String getSelectedMision() {
		// autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		return autoChooser.getSelected();
	}

	public AutoMission getAutoMision() {
		// autoSelected = SmartDashboard.getString("Auto
		// Selector",AutoMission.passLine.name());
		//this is also commented because it's still yelling at me I hate this robot sometimes and the feeling is mutual
		String missionString = autoChooser.getSelected();
		if (missionString.equals(AutoMission.rightSwitch.name()))
			return AutoMission.rightSwitch;
		if (missionString.equals(AutoMission.leftSwitch.name()))
			return AutoMission.leftSwitch;
		if (missionString.equals(AutoMission.switchMySide.name()))
			return AutoMission.switchMySide;
		if (missionString.equals(AutoMission.switchEitherSide.name()))
			return AutoMission.switchEitherSide;
		if (missionString.equals(AutoMission.scaleMySide.name()))
			return AutoMission.scaleMySide;
		if (missionString.equals(AutoMission.leftScale.name()))
			return AutoMission.leftScale;
		if (missionString.equals(AutoMission.rightScale.name()))
			return AutoMission.rightScale;
		if (missionString.equals(AutoMission.scaleOrSwitch.name()))
			return AutoMission.scaleOrSwitch;
		if (missionString.equals(AutoMission.switchOrScale.name()))
			return AutoMission.switchOrScale;
		return AutoMission.passLine;
	}

	public StartPosition getStartPosition() {
		// String positionSelected = SmartDashboard.getString("Starting
		// Position",defaultAuto, StartPosition.right.name());
		positionChooser = (SendableChooser<String>) SmartDashboard.getData("Auto choices");
		String positionString = positionChooser.getSelected();
		
		if (positionString.equals(StartPosition.left.name()))
			return StartPosition.left;
		if (positionString.equals(StartPosition.right.name()))
			return StartPosition.right;
		if (positionString.equals(StartPosition.farRight.name()))
			return StartPosition.farRight;
		return StartPosition.right;
	}

	public void pushDevinDrive() {
		SmartDashboard.putBoolean("DevinDrive", RobotMap.Drivetrain.DevinDrive);
	}

	public boolean getDevinMode() {
		return SmartDashboard.getBoolean("DevinDrive", RobotMap.Drivetrain.DevinDrive);
	}

	public void pushElevatorPID() {
		SmartDashboard.putNumber("EkP", RobotMap.Elevator.kP);
		SmartDashboard.putNumber("EkI", RobotMap.Elevator.kI);
		SmartDashboard.putNumber("EkD", RobotMap.Elevator.kD);
		SmartDashboard.putNumber("Etolerance", RobotMap.Elevator.tolerance);
		SmartDashboard.putNumber("Emin output", RobotMap.Elevator.outputMin);
		SmartDashboard.putNumber("Emax output", RobotMap.Elevator.outputMax);
	}

	public double getElevatorKP() {
		return SmartDashboard.getNumber("EkP", RobotMap.Elevator.kP);
	}

	public double getElevatorKI() {
		return SmartDashboard.getNumber("EkI", RobotMap.Elevator.kI);
	}

	public double getElevatorKD() {
		return SmartDashboard.getNumber("EkD", RobotMap.Elevator.kD);
	}

	public double getTolerance() {
		return SmartDashboard.getNumber("Etolerance", RobotMap.Elevator.tolerance);
	}

	public double getOutputMin() {
		return SmartDashboard.getNumber("Emin output", RobotMap.Elevator.outputMin);
	}

	public double getOutputMax() {
		return SmartDashboard.getNumber("Emax output", RobotMap.Elevator.outputMax);
	}

	public void pushTurnPID() {
		SmartDashboard.putNumber("TkP", RobotMap.TurnDegrees.kP);
		SmartDashboard.putNumber("TkI", RobotMap.TurnDegrees.kI);
		SmartDashboard.putNumber("TkD", RobotMap.TurnDegrees.kD);

	}

	public double getTurnKP() {
		return SmartDashboard.getNumber("TkP", RobotMap.Elevator.kP);
	}

	public double getTurnKI() {
		return SmartDashboard.getNumber("TkI", RobotMap.Elevator.kI);
	}

	public double getTurnKD() {
		return SmartDashboard.getNumber("TkD", RobotMap.Elevator.kD);
	}

	public void pushPID(PDController pid) {
		if (null == pid)
			return;
		 SmartDashboard.putNumber("PIDsetPoint", pid.getSetPoint());
		 SmartDashboard.putNumber("PIDerror", pid.getError());
	}

	public void pushGyro() {
		SmartDashboard.putNumber("Gyro Reading", DriveTrain.getInstance().getAngle());
	}
	
	public boolean doSecondaryMission() {
		return SmartDashboard.getBoolean("Auto Position For Second Cube", RobotMap.Autonomous.SecondaryCube);
	}
	
}
