package org.usfirst.frc.team4213.robot.controllers;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;

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

	public boolean isElevatorUp() {
		return operator.getYButton();
	}

	public boolean isElevatorDown() {
		return operator.getXButton();
	}

}
