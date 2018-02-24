package org.usfirst.frc.team4213.robot.controllers;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;

public class MasterControls {
	private static final MasterControls instance = new MasterControls();
	private static final Logger logger = Logger.getLogger(MasterControls.class.getName());

	private static final XboxControllerMetalCow driver = new XboxControllerMetalCow(RobotMap.DriverController.USB_PORT);
	private static final XboxControllerMetalCow operator = new XboxControllerMetalCow(RobotMap.OperatorController.USB_PORT);

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
		return operator.getRT() > .25;
	}

	public boolean isCubeEject() {
		return operator.getLT() > .25;
	}

	// public boolean isElevatorUp() {
	// return operator.getLT();
	// }

	// public boolean isElevatorDown() {
	// return operator.getRT();

	public double raiseElevator() {
		return operator.getRT();
	}

	public double lowerElevator() {
		return operator.getLT();

	}

	public double getElevatorThrottle() {
		return (Math.abs(operator.getRY()) > .01) ? operator.getRY() : 0;
	}
	
	public boolean raiseIntake() {
		return operator.getYButton();
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
		return (Math.abs(operator.getLY())>.01) ? operator.getLY() : 0;
	}
	public boolean climbEnabled() {
		return operator.getLB();
	}
	public boolean isTitltUp() {
		return operator.getBButton();
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