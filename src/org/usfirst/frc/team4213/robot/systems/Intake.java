package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MaxBotixRangeFinder;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Intake {
	private static final Intake instance = new Intake();
	private static final Logger logger = Logger.getLogger(Intake.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();
	private static final Elevator elevator = Elevator.getInstance();
	private static final Talon LEFT_INTAKE_MOTOR = new Talon(RobotMap.Intake.LEFT_MOTOR_CHANNEL);
	private static final Talon RIGHT_INTAKE_MOTOR = new Talon(RobotMap.Intake.RIGHT_MOTOR_CHANNEL);

	private DigitalInput upSensor = new DigitalInput(RobotMap.Intake.LIMIT_SWITCH_UP);
	private DigitalInput downSensor = new DigitalInput(RobotMap.Intake.LIMIT_SWITCH_DOWN);
	MaxBotixRangeFinder cubeSensor = new MaxBotixRangeFinder(RobotMap.Intake.RANGE_FINDER);

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
			System.out.println("om nom nomming");
		} else if (controller.isCubeEject()) {
			powerCubeEject();
		} else {
			powerCubeIdle();
		}

		/// Do stuff with the range finder and whatever

	}

	private void powerCubeIntake() {
		LEFT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.INTAKE_SPEED);
		RIGHT_INTAKE_MOTOR.setSpeed(-RobotMap.Intake.INTAKE_SPEED);
		currentIntakeState = IntakeState.IN;
	}

	private void powerCubeEject() {
		LEFT_INTAKE_MOTOR.setSpeed(RobotMap.Intake.EJECT_SPEED);
		RIGHT_INTAKE_MOTOR.setSpeed(-RobotMap.Intake.EJECT_SPEED);
		currentIntakeState = IntakeState.OUT;
	}

	private void powerCubeIdle() {
		if (IntakeState.OFF == currentIntakeState) {
			return;
		}
		LEFT_INTAKE_MOTOR.stopMotor();
		RIGHT_INTAKE_MOTOR.stopMotor();
		currentIntakeState = IntakeState.OFF;
	}

	private boolean isCubeSensorSwitchActive() {
		return cubeSensor.getDistanceInches() < 12;
		// return cubeSensorSwitch.get();
	}

	private boolean isIntakeUp() {
		return upSensor.get();
	}

	private boolean isIntakeDown() {
		return downSensor.get();
	}

}
