package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Intake {
	private static final Talon LEFT_INTAKE_MOTOR = new Talon(RobotMap.Intake.LEFT_MOTOR_CHANNEL);
	DigitalInput limitSwitch = new DigitalInput(RobotMap.Intake.LIMIT_SWITCH_CHANNEL);

	String elevator;
	private IntakeState currentIntakeState = IntakeState.OFF; // start state is off

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
		if (IntakeState.IN == currentIntakeState && isSwitchOpen())
			return;
		if (isSwitchOpen()) {
			LEFT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.INTAKE_SPEED);
			currentIntakeState = IntakeState.IN;
		} else {
			powerCubeIdle();
		}

	}

	private boolean isSwitchOpen() {
		return limitSwitch.get() == false;
	}

	public void powerCubeEject() {
		if (IntakeState.OUT == currentIntakeState)
			return;
		LEFT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.EJECT_SPEED);
		currentIntakeState = IntakeState.OUT;
	}

	public void powerCubeIdle() {
		if (IntakeState.OFF == currentIntakeState)
			return;
		LEFT_INTAKE_MOTOR.stopMotor();
		currentIntakeState = IntakeState.OFF;
	}

	// enums
	private enum IntakeState {
		OFF, IN, OUT
	}

}
