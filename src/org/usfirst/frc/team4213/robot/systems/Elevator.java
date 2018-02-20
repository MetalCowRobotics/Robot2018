package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import java.util.logging.Logger;
public class Elevator {
	private static final Elevator instance = new Elevator();
	private static final Intake intake = Intake.getInstance();
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();
	
//e2
	// private static final Talon ELEVATOR_MOTOR = new
	// Talon(RobotMap.Elevator.ELEVATOR_CHANNEL);
	private static final MCR_SRX ELEVATOR_MOTOR1 = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1);
	private static final MCR_SRX ELEVATOR_MOTOR2 = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL2);
	private static SpeedControllerGroup ElevatorSpeedControllerGroup = new SpeedControllerGroup (ELEVATOR_MOTOR1, ELEVATOR_MOTOR2);

	private static final Encoder elevatorEncoder = new Encoder(RobotMap.Elevator.ELEVATOR_ENCODER_1, RobotMap.Elevator.ELEVATOR_ENCODER_2, false, CounterBase.EncodingType.k4X);

	DigitalInput topLimit;// = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	DigitalInput bottomLimit;// = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);

	MotorState motorState = MotorState.OFF; // start state is off
	ElevatorState elevatorState = ElevatorState.BOTTOM;
	boolean AutoPosition = false;
	double encoderTarget;

	private Elevator() {
		// Singleton Pattern
	}

	public void execute() {
		System.out.println("elevator encoder tics:" + getEncoderTics());
		if (AutoPosition) {
			System.out.println("elevator encode:" + getEncoderTics());
			System.out.println("elevator target:" + encoderTarget);
			System.out.println("elevator boolean:" + (getEncoderTics() > encoderTarget));
			if (getEncoderTics() < encoderTarget) {
				stop();
				AutoPosition = false;
			}
		} else if (controller.isElevatorDown()) {
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
		if (!movingUp()) {
			setElevatorSpeed(RobotMap.Elevator.UP_SPEED);
		}
		// elevatorState = topLimit.get() ? ElevatorState.TOP : ElevatorState.MIDDLE;
	}

	public void moveDown() {
		if (!movingDown()) {
			setElevatorSpeed(RobotMap.Elevator.DOWN_SPEED);
		}
		// elevatorState = bottomLimit.get() ? ElevatorState.BOTTOM :
		// ElevatorState.MIDDLE;
	}

	private boolean movingUp() {
		return MotorState.UP == motorState;
	}

	private boolean movingDown() {
		return MotorState.DOWN == motorState;
	}

	private void setElevatorSpeed(double speed) {
		motorState = (speed < 0) ? MotorState.DOWN : MotorState.UP;
		ElevatorSpeedControllerGroup.set(speed);
	}

	public void stop() {
		ElevatorSpeedControllerGroup.stopMotor();
		motorState = MotorState.OFF;
	}

	public void moveElevatortopostion() {
		moveElevatorToPosition(RobotMap.Elevator.EXCHANGE_HEIGHT);
	}

	public void moveElevatorToPosition(double height) {
		AutoPosition = true;
		encoderTarget = getEncoderTics() - height;
		moveUp();
		System.out.println("elevator target tics" + encoderTarget);
	}

	private double getEncoderTics() {
		//return ELEVATOR_MOTOR2.getSensorCollection().getQuadraturePosition();
		return elevatorEncoder.getDistance();
	}
	
	private enum MotorState {
		OFF, UP, DOWN
	}

	private enum ElevatorState {
		TOP, MIDDLE, BOTTOM
	}
}
