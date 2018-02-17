package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.DriveTrain;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.Intake;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public abstract class Mission {

	DriverStation driverStation = DriverStation.getInstance();
	DriveTrain driveTrain = DriveTrain.getInstance();
	Intake intake = Intake.getInstance();
	Elevator elevator = Elevator.getInstance();

	// state machine steps

	// make private step methods one(), two()
	// use state machine to track what step you are on

	// by making named missions that extend Mission we should be able to call
	// whatever we need

	private Hand getNearSwitch() {
		return driverStation.getGameSpecificMessage().toUpperCase().charAt(0) == 'L' ? Hand.kLeft : Hand.kRight;
	}

	private Hand getScale() {
		return driverStation.getGameSpecificMessage().toUpperCase().charAt(1) == 'L' ? Hand.kLeft : Hand.kRight;
	}

	private Hand getFarSwitch() {
		return driverStation.getGameSpecificMessage().toUpperCase().charAt(2) == 'L' ? Hand.kLeft : Hand.kRight;
	}

	protected boolean onMySwitchSide(Hand mySide) {
		return mySide == getNearSwitch();
	}

	protected boolean onMyScaleSide(Hand mySide) {
		return mySide == getScale();
	}

	public abstract void execute();

}
