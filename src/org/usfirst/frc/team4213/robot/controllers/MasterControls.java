package org.usfirst.frc.team4213.robot.controllers;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;

public class MasterControls {
	private static final Logger logger = Logger.getLogger(MasterControls.class.getName());
	private static final double throttleVariance = .06;
	private static final MasterControls instance = new MasterControls();

	private static final XboxControllerMetalCow driver = new XboxControllerMetalCow(RobotMap.DriverController.USB_PORT);
	private static final XboxControllerMetalCow operator = new XboxControllerMetalCow(RobotMap.OperatorController.USB_PORT);

	private MasterControls() {
		// Intentionally Blank for Singleton
		logger.setLevel(RobotMap.LogLevels.masterControlsClass);
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
		return driver.getXButton();
	}

	public boolean isCrawlToggle() {
		return driver.getAButton();
	}

	public boolean invertDrive() {
		return driver.getYButtonPressed();
	}

	public boolean isCubeIntake() {
		return operator.getLT() > .25;
	}

	public boolean isCubeEject() {
		return operator.getRT() > .25;
	}

	public double getElevatorThrottle() {
		return (Math.abs(operator.getRY()) > throttleVariance) ? operator.getRY() : 0;
	}
	
	public boolean raiseIntake() {
		return operator.getYButton();
	}

	public boolean lowerIntake() {
		return operator.getAButton();
	}

	public void intakeRumbleOn() {
		operator.rumbleLeft(0.5);
	}

	public void intakeRumbleOff() {
		operator.rumbleLeft(0);
	}

	public double getClimbThrottle() {
		//add tolerance near 0
		return (Math.abs(operator.getLY())>.02) ? -operator.getLY() : 0;
	}
	
	public boolean isClimberActivated() {
		return operator.getLB();
	}
	
		
	public boolean isTitltUp() {
		return operator.getYButton();
	}
	
	public boolean isTiltDown() {
		return operator.getAButton();
	}
	public double forwardSpeed() {
		return driver.getRT();
	}

	public double reverseSpeed() {
		return driver.getLT();
	}
	
	public double direction() {
		return driver.getLX();
	}
	
}