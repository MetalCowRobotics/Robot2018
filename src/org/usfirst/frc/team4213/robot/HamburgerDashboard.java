package org.usfirst.frc.team4213.robot;

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
	}

	public void pushAutonomousMissions() {
		// Load available Autonomous missions to the driverstation
		autoSelected = rightSide;
		// autoChooser.addObject("Right Side Switch", rightSide);
		// autoChooser.addObject("Left Side Switch", leftSide);
		// autoChooser.addDefault("Pass Line", passLine);
		// autoChooser.addObject("Switch Either Side", eitherSide);
		// autoChooser.addObject("Left Scale", leftSideOfScale );
		// autoChooser.addObject("RightPosition", rightPosition);
		// autoChooser.addObject("LeftPosition", leftPosition);
		// autoChooser.addObject("MiddlePosition", middlePosition);
		autoChooser.addDefault("Pass Line", AutoMission.passLine.name());
		autoChooser.addObject("Right Side Switch", AutoMission.rightSwitch.name());
		autoChooser.addObject("Left Side Switch", AutoMission.leftSwitch.name());
		autoChooser.addObject("Switch Either Side", AutoMission.switchEitherSide.name());
		autoChooser.addObject("Left Scale", AutoMission.leftScale.name());
		autoChooser.addObject("Right Scale", AutoMission.rightScale.name());
		autoChooser.addObject("Scale or Switch My Side", AutoMission.scaleOrSwitch.name());
		autoChooser.addObject("Switch or Scale My Side", AutoMission.switchOrScale.name());
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

	public void pushPID() {
		SmartDashboard.putNumber("kP", RobotMap.Elevator.kP);
		SmartDashboard.putNumber("kI", RobotMap.Elevator.kI);
		SmartDashboard.putNumber("kD", RobotMap.Elevator.kD);
		SmartDashboard.putNumber("tolerance", RobotMap.Elevator.tolerance);
		SmartDashboard.putNumber("min output", RobotMap.Elevator.outputMin);
		SmartDashboard.putNumber("max output", RobotMap.Elevator.outputMax);
	}
	public double getKP() {
		return SmartDashboard.getNumber("kP", RobotMap.Elevator.kP);
	}
	public double getKI() {
		return SmartDashboard.getNumber("kI", RobotMap.Elevator.kI);
	}
	public double getKD() {
		return SmartDashboard.getNumber("kD", RobotMap.Elevator.kD);
	}
	public double getTolerance() {
		return SmartDashboard.getNumber("tolerance", RobotMap.Elevator.tolerance);
	}
	public double getOutputMin() {
		return SmartDashboard.getNumber("min output", RobotMap.Elevator.outputMin);
	}
	
	public double getOutputMax() {
		return SmartDashboard.getNumber("max output", RobotMap.Elevator.outputMax);
	}
	
	public void pushPID(PIDController pid) {
		if (null == pid) return;
		SmartDashboard.putBoolean("PIDenabled", pid.isEnabled());
		SmartDashboard.putNumber("PIDsetPoint", pid.getSetpoint());
		SmartDashboard.putNumber("PIDerror", pid.getError());
		SmartDashboard.putBoolean("PIDonTarget", pid.onTarget());
		
	}
	
}
