package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Intake {
	private static final Intake instance = new Intake();
	private static final Logger logger = Logger.getLogger(Intake.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();
	// private static final Elevator elevator = Elevator.getInstance();

	private static final SpeedController LEFT_INTAKE_MOTOR = new Talon(RobotMap.Intake.LEFT_MOTOR_CHANNEL);
	private static final SpeedController RIGHT_INTAKE_MOTOR = new Talon(RobotMap.Intake.RIGHT_MOTOR_CHANNEL);

	private static final MCR_SRX INTAKE_ANGLE_MOTOR = new MCR_SRX(RobotMap.Intake.ANGLE_MOTOR_CHANNEL);

	// private MaxBotixRangeFinder cubeSensor = new
	// MaxBotixRangeFinder(RobotMap.Intake.RANGE_FINDER);
	private static final DigitalInput cubeSwitch = new DigitalInput(RobotMap.Intake.BOX_SENSOR);

	private enum IntakeState {
		OFF, IN, OUT
	}

	private IntakeState currentIntakeState = IntakeState.OFF; // start state is off

	private Timer ejectTimer = new Timer();
	private boolean autoIntake = false;
	private boolean autoEject = false;
	private Timer deployTimer = new Timer();
	private boolean autoDeploy = false;

	private Intake() {
		// Singleton Pattern

	}

	public static Intake getInstance() {
		return instance;
	}

	public void execute() {

		System.out.println("   Intake Up: " + isIntakeUp() + "  Down: " + isIntakeDown() + "  BoxDetected:"
				+ this.isCubeSensorSwitchActive());

		if (autoEject) {
			if (ejectTimer.get() > RobotMap.Intake.AUTO_EJECT_SECONDS) {
				ejectTimer.stop();
				powerCubeIdle();
				autoEject = false;
			}
		} else if (autoIntake) {
			if (isCubeSensorSwitchActive()) {
				powerCubeIdle();
				autoIntake = false;
			}
		} else {
			if (controller.isCubeIntake()) {
				powerCubeIntake();
			} else if (controller.isCubeEject()) {
				powerCubeEject();
			} else {
				powerCubeIdle();
			}
		}

		// intake angle raise and lower
		if (controller.isTiltDown()) {
			deploy();
		} else if (controller.isTitltUp()) {
			INTAKE_ANGLE_MOTOR.set(RobotMap.Intake.RAISE_INTAKE_SPEED);
		} else {
			if (autoDeploy) {
				if (deployTimer.get() > 1) {
					stopIntakeDeploy();
					deployTimer.stop();
					autoDeploy = false;
				}
			} else {
				stopIntakeDeploy();
			}
		}

	}

	public void stopIntakeDeploy() {
		INTAKE_ANGLE_MOTOR.stopMotor();
	}

	public void autoEject() {
		autoEject = true;
		powerCubeEject();
		ejectTimer.reset();
		ejectTimer.start();
	}

	public void autoIntake() {
		autoIntake = true;
		powerCubeIntake();
	}

	private void powerCubeIntake() {
		currentIntakeState = IntakeState.IN;
		if (isCubeSensorSwitchActive()) {
			controller.intakeRumbleOn();
			powerCubeIdle();
		} else {
			LEFT_INTAKE_MOTOR.set(-RobotMap.Intake.INTAKE_SPEED);// .setSpeed(RobotMap.Intake.INTAKE_SPEED);
			RIGHT_INTAKE_MOTOR.set(RobotMap.Intake.INTAKE_SPEED);// setSpeed(RobotMap.Intake.INTAKE_SPEED);
		}
	}

	private void powerCubeEject() {
		LEFT_INTAKE_MOTOR.set(-RobotMap.Intake.EJECT_SPEED);// .setSpeed(RobotMap.Intake.EJECT_SPEED);
		RIGHT_INTAKE_MOTOR.set(RobotMap.Intake.EJECT_SPEED);// .setSpeed(RobotMap.Intake.EJECT_SPEED);
		currentIntakeState = IntakeState.OUT;
	}

	private void powerCubeIdle() {
		if (IntakeState.OFF == currentIntakeState) {
			return;
		}
		LEFT_INTAKE_MOTOR.stopMotor();
		RIGHT_INTAKE_MOTOR.stopMotor();
		currentIntakeState = IntakeState.OFF;
		controller.intakeRumbleOff();
	}

	public boolean isIntakeRunning() {
		return IntakeState.OFF != currentIntakeState;
	}

	public boolean isCubeSensorSwitchActive() {
		return !cubeSwitch.get();
		// return cubeSensor.getDistanceInches(mail) < 12;
		// return cubeSensorSwitch.get();
	}

	public void autoDeploy() {
		deployTimer.reset();
		deployTimer.start();
		deploy();
	}
	
	private void deploy() {
		INTAKE_ANGLE_MOTOR.set(RobotMap.Intake.LOWER_INTAKE_SPEED);
	}

	public boolean isIntakeUp() {
		return INTAKE_ANGLE_MOTOR.getSensorCollection().isFwdLimitSwitchClosed();
	}

	public boolean isIntakeDown() {
		return INTAKE_ANGLE_MOTOR.getSensorCollection().isRevLimitSwitchClosed();

	}

}
