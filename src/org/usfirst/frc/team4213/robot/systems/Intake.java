package org.usfirst.frc.team4213.robot.systems;


import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.lib14.MaxBotixRangeFinder;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team4213.lib14.MaxBotixRangeFinder;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;
import org.usfirst.frc.team4213.lib14.MCR_SRX;

import java.util.logging.Logger;

public class Intake {
	private static final Intake instance = new Intake();
	private static final Logger logger = Logger.getLogger(Intake.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();
	// private static final Elevator elevator = Elevator.getInstance();

	// TODO static or not static?
	private static final SpeedController LEFT_INTAKE_MOTOR = new Talon(RobotMap.Intake.LEFT_MOTOR_CHANNEL);
	private static final SpeedController RIGHT_INTAKE_MOTOR = new Talon(RobotMap.Intake.RIGHT_MOTOR_CHANNEL);

	private DigitalInput upSensor = new DigitalInput(RobotMap.Intake.LIMIT_SWITCH_UP);
	private DigitalInput downSensor = new DigitalInput(RobotMap.Intake.LIMIT_SWITCH_DOWN);

	private static final MCR_SRX INTAKE_ANGLE_MOTOR = new MCR_SRX(RobotMap.Intake.ANGLE_MOTOR_CHANNEL);

	private MaxBotixRangeFinder cubeSensor = new MaxBotixRangeFinder(RobotMap.Intake.RANGE_FINDER);
	private static final DigitalInput cubeSwitch = new DigitalInput(RobotMap.Intake.BOX_SENSOR);

	private enum IntakeState {
		OFF, IN, OUT
	}

	private IntakeState currentIntakeState = IntakeState.OFF; // start state is off

	private Timer timer = new Timer();
	private boolean autoIntake = false;
	private boolean autoEject = false;

	private Intake() {
		// Singleton Pattern

	}

	public static Intake getInstance() {
		return instance;
	}

	public void execute() {
				
		System.out.println("   Intake Up: "+isIntakeUp()+"  Down: "+isIntakeDown()+"  BoxDetected:"+this.isCubeSensorSwitchActive());
		
		if (autoEject) {
			if (timer.get() > RobotMap.Intake.AUTO_EJECT_SECONDS) {
				timer.stop();
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

		//intake raise and lower
		if(controller.isCrawlToggle()) {
			deploy();
		} else if (controller.isSprintToggle()) {
			INTAKE_ANGLE_MOTOR.set(RobotMap.Intake.RAISE_INTAKE_SPEED);
		} else {
			RaiseIntakeMotor.stopMotor();
		}

	}

	public void autoEject() {
		autoEject = true;
		powerCubeEject();
		timer.reset();
		timer.start();
	}

	public void autoIntake() {
		autoIntake = true;
		powerCubeIntake();
	}

	private void powerCubeIntake() {
		// TODO use invert motor
		LEFT_INTAKE_MOTOR.set(-RobotMap.Intake.INTAKE_SPEED);//.setSpeed(RobotMap.Intake.INTAKE_SPEED);
		RIGHT_INTAKE_MOTOR.set(RobotMap.Intake.INTAKE_SPEED);//setSpeed(RobotMap.Intake.INTAKE_SPEED);
		currentIntakeState = IntakeState.IN;
		if (isCubeSensorSwitchActive()) {
			controller.intakeRumbleOn();
		}
	}

	private void powerCubeEject() {
		LEFT_INTAKE_MOTOR.set(-RobotMap.Intake.EJECT_SPEED);//.setSpeed(RobotMap.Intake.EJECT_SPEED);
		RIGHT_INTAKE_MOTOR.set(RobotMap.Intake.EJECT_SPEED);//.setSpeed(RobotMap.Intake.EJECT_SPEED);
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
		System.out.println("cubeSensor=" + cubeSensor.getDistanceInches());
		return !cubeSwitch.get();
		//return cubeSensor.getDistanceInches() < 12;
		// return cubeSensorSwitch.get();
	}

	public void deploy() {
		RaiseIntakeMotor.set(RobotMap.Intake.LOWER_INTAKE_SPEED);
	}

	private boolean isIntakeUp() {
		return INTAKE_ANGLE_MOTOR.getSensorCollection().isFwdLimitSwitchClosed();
	}

	private boolean isIntakeDown() {
		return INTAKE_ANGLE_MOTOR.getSensorCollection().isRevLimitSwitchClosed();

	}

}
