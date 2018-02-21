package org.usfirst.frc.team4213.robot.controllers;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID;

public class MasterControls {
	private static final MasterControls instance = new MasterControls();
	private static final Logger logger = Logger.getLogger(MasterControls.class.getName());

	private static final XboxControllerMetalCow driver = new XboxControllerMetalCow(RobotMap.DriverController.USB_PORT);
	private static final XboxControllerMetalCow operator = new XboxControllerMetalCow(
			RobotMap.OperatorController.USB_PORT);

	private MasterControls() {
		// Intentionally Blank for Singleton
	}

	public static MasterControls getInstance() {
		return instance;
	}

	public double getDriveLeftThrottle() {
		return driver.getLY();
	}

	public double getDriveRightThrottle() {
		return driver.getRY();
	}

	// Arcade
	public boolean isHalfArcadeToggle() {
		return driver.getAButton();
	}

	public boolean isSprintToggle() {
		return driver.getRB();
	}

	public boolean isCrawlToggle() {
		return driver.getLB();
	}

	public boolean invertDrive() {
		return driver.getYButtonPressed();
	}

	public boolean isCubeIntake() {
		return operator.getRB();
	}

	public boolean isCubeEject() {
		return operator.getLB();
	}

<<<<<<< HEAD
	public boolean isElevatorUp() {
		return operator.getLT();
	}

	public boolean isElevatorDown() {
		return operator.getRT();
=======
	public boolean raiseElevator() {
		return operator.getYButton();
	}

	public boolean lowerElevator() {
		return operator.getXButton();
>>>>>>> master
	}
	public boolean raiseIntake() {
		return operator.getBButton();
	}

	public boolean lowerIntake() {
		return operator.getAButton();
	}
	public void intakeRumbleOn() {
		operator.rumbleLeft(0.5);
		System.out.println("rumble on");
	}
	public void intakeRumbleOff() {
		operator.rumbleLeft(0);
	}
	public double getClimbThrottle() {
		return operator.getLY();
	}
}