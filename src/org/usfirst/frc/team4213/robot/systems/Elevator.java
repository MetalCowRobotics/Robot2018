package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.RobotMap;
import edu.wpi.first.wpilibj.Talon;

public class Elevator {
	private static final Talon STAGE_1_MOTOR = new Talon(RobotMap.Elevator.STAGE_1_MOTOR_CHANNEL);
	private static final Talon STAGE_2_MOTOR = new Talon(RobotMap.Elevator.STAGE_2_MOTOR_CHANNEL);
	MotorState currentStage1State = MotorState.OFF; // start state is off
	MotorState currentStage2State = MotorState.OFF; // start state is off

	ElevatorState curStage1Location = ElevatorState.BOTTOM;
	ElevatorState curStage2Location = ElevatorState.BOTTOM;

	public void moveUp() {
		if (!movingUp()) {
			return;
		}
		if (ElevatorState.TOP != curStage1Location) {
			STAGE_1_MOTOR.setSpeed(RobotMap.Elevator.UP_SPEED);
			currentStage1State = MotorState.UP;
		} else if (ElevatorState.TOP != curStage2Location) {
			STAGE_2_MOTOR.setSpeed(RobotMap.Elevator.UP_SPEED);
			currentStage2State = MotorState.UP;
		}
	}

	public void moveDown() {
		if (!movingDown()) {
			return;
		}
		if (ElevatorState.BOTTOM != curStage2Location) {
			STAGE_2_MOTOR.setSpeed(RobotMap.Elevator.DOWN_SPEED);
			currentStage2State = MotorState.DOWN;
		} else if (ElevatorState.BOTTOM != curStage1Location) {
			STAGE_1_MOTOR.setSpeed(RobotMap.Elevator.DOWN_SPEED);
			currentStage1State = MotorState.DOWN;
		}
	}

	private boolean movingUp() {
		if (MotorState.UP == currentStage1State || MotorState.UP == currentStage2State) {
			return true;
		} else {
			return false;
		}
	}

	private boolean movingDown() {
		if (MotorState.DOWN == currentStage1State || MotorState.DOWN == currentStage2State) {
			return true;
		} else {
			return false;
		}
	}

	public void stop() {
		if (movingUp() || movingDown()) {
			STAGE_1_MOTOR.stopMotor();
			STAGE_2_MOTOR.stopMotor();
			currentStage1State = MotorState.OFF;
			currentStage2State = MotorState.OFF;
		}
	}

	private enum MotorState {
		OFF, UP, DOWN
	}

	private enum ElevatorState {
		TOP, MIDDLE, BOTTOM
	}
}
