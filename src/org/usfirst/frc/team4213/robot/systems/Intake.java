package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.SpeedController;

public class Intake {
	String elevator = "I'm the elevator";
	SpeedController intakeMotor;
	private IntakeState currentIntakeState = IntakeState.OFF; //start state is off
	

	public String getElevator() {
		return elevator;
	}

	public Intake(String elevator) {
		super();
		this.elevator = elevator;
	}

	public void elevatorUp() { 

	}

	public void elevatorDown() {

	}

	public void powerCubeIntake() {
		if(IntakeState.IN == currentIntakeState) 
			return;
		//turn the motors to intake
		currentIntakeState = IntakeState.IN;
	}

	public void powerCubeEject() {
		if(IntakeState.OUT == currentIntakeState) 
			return;
		//turn the motors to eject
		currentIntakeState = IntakeState.OUT;
	}
	public void powerCubeIdle() {
		if(IntakeState.OFF == currentIntakeState)
			return;
		intakeMotor.stopMotor();
		currentIntakeState = IntakeState.OFF;
	}
	private enum IntakeState{
		OFF,IN,OUT
	}

}
