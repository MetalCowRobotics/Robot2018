package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.robot.RobotMap;

import java.util.logging.Logger;

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
	public void intakeRumbleOn() {
		operator.rumbleLeft(0.5);
		System.out.println("rumble on");
	}
	public double getElevatorThrottle() {
		return 0;
	}
	public void intakeRumbleOff() {
		operator.rumbleLeft(0);
	}
	public double getClimbThrottle() {
		return operator.getLY();
	}
}