package org.usfirst.frc.team4213.robot.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

//import org.usfirst.frc.team4213.lib14.Xbox360Controller;

public class DriverController extends XboxController {

	public DriverController(int port) {
		super(port);
	}

	public double getLT() {
		return this.getTriggerAxis(Hand.kLeft);
	}

	public double getRT() {
		return this.getTriggerAxis(Hand.kRight);
	}

	public double getLY() {
		return this.getY(Hand.kLeft);
	}

	public double getRY() {
		return this.getY(Hand.kRight);
	}

	public double getLX() {
		return this.getX(Hand.kLeft);
	}

	public double getRX() {
		return this.getX(Hand.kRight);
	}
	public boolean getLB() {
		return this.getBumper(Hand.kLeft);
	}
	public boolean getRB() {
		return this.getBumper(Hand.kRight);
	}
	public void rumbleLeft(double amt) {
		this.setRumble(RumbleType.kLeftRumble, amt);
	}

	public void rumbleRight(double amt) {
		this.setRumble(RumbleType.kRightRumble, amt);
	}

	public void rumbleAll(double amt) {
		rumbleLeft(amt);
		rumbleRight(amt);
	}

	public void rumbleNone() {
		rumbleLeft(0);
		rumbleRight(0);
	}

}
