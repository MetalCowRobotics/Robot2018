package org.usfirst.frc.team4213.robot.controllers;

public class MasterControls {

	XboxControllerMetalCow driver;
	XboxControllerMetalCow operator;

	public MasterControls(XboxControllerMetalCow driver, XboxControllerMetalCow operator) {
		this.driver = driver;
		this.operator = operator;
	}

	public double getDriveLeftThrottle() {
		return driver.getLY();
	}

	public double getDriveRightThrottle() {
		return driver.getRY();
	}

	public boolean isHalfArcadeToggle() {
		return driver.getAButton();
	}

	public boolean isSprintToggle() {
		return driver.getRB();
	}
	
	public boolean isCrawlToggle() {
		return driver.getLB();
	}

	public boolean isCubeIntake() {
		// TODO Auto-generated method stub
		// operator.getSomeButton check and return....
		return false;
	}

	public boolean isCubeEject() {
		// TODO Auto-generated method stub
		// operator.getSomeButton check and return....
		return false;
	}
		
}
