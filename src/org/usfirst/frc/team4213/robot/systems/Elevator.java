package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Elevator {
	private static final Elevator instance = new Elevator();
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();

	private static final Talon ELEVATOR_MOTOR = new Talon(RobotMap.Elevator.ELEVATOR_CHANNEL);

	DigitalInput topLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	DigitalInput bottomLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);

	MotorState motorState = MotorState.OFF; // start state is off
	ElevatorState elevatorState = ElevatorState.BOTTOM;

	private Elevator() {
		// Singleton Pattern
	}

	public void execute() {
		// ToDo:Elevator control code goes here
		if (controller.isElevatorDown()) {
			moveDown();
		} else if (controller.isElevatorUp()) {
			moveUp();
		} else {
			stop();
		}
	}

	public static Elevator getInstance() {
		return instance;
	}

	public void moveUp() {
		if (!movingUp() && ElevatorState.TOP != elevatorState) {
			setElevatorSpeed(RobotMap.Elevator.UP_SPEED);
		}
		elevatorState = topLimit.get() ? ElevatorState.TOP : ElevatorState.MIDDLE;
	}

	public void moveDown() {
		if (!movingDown() && ElevatorState.BOTTOM != elevatorState) {
			setElevatorSpeed(RobotMap.Elevator.DOWN_SPEED);
		}
		elevatorState = bottomLimit.get() ? ElevatorState.BOTTOM : ElevatorState.MIDDLE;
	}

	private boolean movingUp() {
		return MotorState.UP == motorState ? true : false;
	}

	private boolean movingDown() {
		return MotorState.DOWN == motorState ? true : false;
	}

	private void setElevatorSpeed(double speed) {
		motorState = (speed < 0) ? MotorState.DOWN : MotorState.UP;
		ELEVATOR_MOTOR.set(speed);
	}

	public void stop() {
		ELEVATOR_MOTOR.stopMotor();
		motorState = MotorState.OFF;
	}

	private enum MotorState {
		OFF, UP, DOWN
	}

	private enum ElevatorState {
		TOP, MIDDLE, BOTTOM
	}
}
