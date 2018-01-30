package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Intake {
	private static final Intake instance = new Intake();

	private static final MasterControls controller = MasterControls.getInstance();
	private static final Elevator elevator = Elevator.getInstance();
	private static final Talon LEFT_INTAKE_MOTOR = new Talon(RobotMap.Intake.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_INTAKE_MOTOR = LEFT_INTAKE_MOTOR;
	// private static final Talon RIGHT_INTAKE_MOTOR = new Talon(RobotMap.Intake.RIGHT_MOTOR_CHANNEL);

	DigitalInput cubeSensorSwitch = new DigitalInput(RobotMap.Intake.LIMIT_SWITCH_CHANNEL);

	private enum IntakeState {
		OFF, IN, OUT
	};

	private IntakeState currentIntakeState = IntakeState.OFF; // start state is off

	private Intake() {
		// Singleton Pattern
	}

	public static Intake getInstance() {
		return instance;
	}

	public void execute() {
		if (controller.isCubeIntake()) {
			powerCubeIntake();
		} else if (controller.isCubeEject()) {
			powerCubeEject();
		} else {
			powerCubeIdle();
		}
	}

	private void powerCubeIntake() {
		if (IntakeState.IN == currentIntakeState && !isCubeSensorSwitchActive()) {
			return;
		}
		if (isCubeSensorSwitchActive()) {
			LEFT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.INTAKE_SPEED);
			RIGHT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.INTAKE_SPEED);
			currentIntakeState = IntakeState.IN;
		} else {
			powerCubeIdle();
		}

	}

	public void powerCubeEject() {
		if (IntakeState.OUT == currentIntakeState) {
			return;
		}
		LEFT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.EJECT_SPEED);
		RIGHT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.EJECT_SPEED);
		currentIntakeState = IntakeState.OUT;
	}

	public void powerCubeIdle() {
		if (IntakeState.OFF == currentIntakeState) {
			return;
		}
		LEFT_INTAKE_MOTOR.stopMotor();
		RIGHT_INTAKE_MOTOR.stopMotor();
		currentIntakeState = IntakeState.OFF;
	}

	private boolean isCubeSensorSwitchActive() {
		return cubeSensorSwitch.get();
	}

}
