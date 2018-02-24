package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
public class Elevator {
	private static final Elevator instance = new Elevator();
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();
	
	private static SpeedControllerGroup ELEVATOR_MOTOR = new SpeedControllerGroup (new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1), new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL2));

	private static final Encoder elevatorEncoder = new Encoder(RobotMap.Elevator.ELEVATOR_ENCODER_1, RobotMap.Elevator.ELEVATOR_ENCODER_2, false, CounterBase.EncodingType.k4X);

	DigitalInput topLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	DigitalInput bottomLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);

	MotorState motorState = MotorState.OFF; // start state is off
	ElevatorState elevatorState = ElevatorState.BOTTOM;
	boolean AutoPosition = false;
	double encoderTarget;
	double currentPosition;
	double bottomTics;
	double topTics;
	//double safteyZone = (12 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
	//double safteyTopSpeed = .5;
	PIDController holdPID; 
	boolean firstTime = true;
	
	
	private Elevator() {
		// Singleton Pattern
	}

	public void execute() {
		logger.fine("==================== elevator execute ====================");
		initElevator();
		logParameters();
		logPID();
		logParameters();
		if (AutoPosition) {
			logger.info("elevator boolean:" + (getEncoderTics() > encoderTarget));
			if (getEncoderTics() >= encoderTarget) {
				stop();
				AutoPosition = false;
			}
		} else {
			double elevatorSpeed = controller.getElevatorThrottle();
			if (isElevatorAtTop() && elevatorSpeed > 0) {
				stop();
			} else if (isElevatorAtBottom() && elevatorSpeed < 0) {
				stop();
			} else {
				moveElevator(elevatorSpeed);
			}
		}

	}

	private void initElevator() {
		if (firstTime) {
			// ELEVATOR_MOTOR.setInverted(true);
			startEncoder();
			topTics = bottomTics + (72 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
			logger.info("bottomTics:" + bottomTics);
			logger.info("topTics:" + topTics);
			firstTime = false;
		}
	}

	public static Elevator getInstance() {
		return instance;
	}

	private void moveElevator(double elevatorSpeed) {
		//rampe the elevator speeds near the top and bottom
		if (getEncoderTics() > (topTics - RobotMap.Elevator.SAFTEY_ZONE) && elevatorSpeed > 0) {
			disablePID();
			ELEVATOR_MOTOR.set(Math.min(elevatorSpeed, RobotMap.Elevator.SAFE_SPEED));
			motorState = MotorState.ON;
		} else if (getEncoderTics() < (bottomTics + RobotMap.Elevator.SAFTEY_ZONE) && elevatorSpeed < 0) {
			disablePID();
			ELEVATOR_MOTOR.set(Math.max(elevatorSpeed, -RobotMap.Elevator.SAFE_SPEED));
			motorState = MotorState.ON;
		} else {	
			if (0 == elevatorSpeed) {
				stop();
			} else {
				disablePID();
				ELEVATOR_MOTOR.set(elevatorSpeed);
				motorState = MotorState.ON;
			}
		}
	}
	
	private void disablePID() {
 		if (null != holdPID)
 			holdPID.disable();
  	}
	
	private void startEncoder() {
		if (null != holdPID)
			holdPID.free();
		holdPID = new PIDController(RobotMap.Elevator.kP, RobotMap.Elevator.kI, RobotMap.Elevator.kD, elevatorEncoder, ELEVATOR_MOTOR); // can add 20ms at end
		holdPID.setOutputRange(RobotMap.Elevator.outputMin, RobotMap.Elevator.outputMax);
		holdPID.setAbsoluteTolerance(RobotMap.Elevator.tolerance);
		bottomTics = getEncoderTics();
	}
	
	private void moveUp() {
		if (!movingUp()) {
			setElevatorSpeed(RobotMap.Elevator.UP_SPEED);
		}
		// elevatorState = topLimit.get() ? ElevatorState.TOP : ElevatorState.MIDDLE;
	}

	private void moveDown() {
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
		ELEVATOR_MOTOR.set(speed);
	}

	public void stop() {
		ELEVATOR_MOTOR.stopMotor();
		motorState = MotorState.OFF;
		//if not at the bottom create a holdPID for the current height
		if (!isElevatorAtBottom()) {
			motorState = MotorState.HOLD;
			startEncoder();
			holdPID.setSetpoint(getEncoderTics());
			holdPID.enable();
		}
	}

	public void moveElevatorToPosition(double height) {
		initElevator();
		holdPID.disable();
		AutoPosition = true;
		encoderTarget = getEncoderTics() + ((height / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION);
		ELEVATOR_MOTOR.set(RobotMap.Elevator.UP_SPEED);
		motorState = MotorState.ON;
		logger.info("moveElevatorToPosition encoderTarget:" + encoderTarget);
	}

	private double getEncoderTics() {
		return elevatorEncoder.getDistance();
	}

	private boolean isElevatorAtTop() {
		return !topLimit.get(); //For some reason this is inverted in the hardware, correcting here in software
	}
	private boolean isElevatorAtBottom() {
		return !bottomLimit.get(); //for some reason this is inverted in hardware, correcting here in software
	}
	
	private enum MotorState {
		ON, OFF, UP, DOWN, HOLD
	}

	private enum ElevatorState {
		TOP, MIDDLE, BOTTOM
	}
	
	private void logParameters() {
		logger.info("Elevator AutoPosition:" + AutoPosition);
		logger.info("Elevator throttle:" + controller.getElevatorThrottle());
		logger.info("Elevator Up limit: " + this.isElevatorAtTop() + " Elevator Down limit: " + this.isElevatorAtBottom());
		logger.info("Speed:" + ELEVATOR_MOTOR.get());
		logger.info("Elevator encoder tics:" + getEncoderTics() + "   Elevator target tics:" + encoderTarget);
		logger.info("Elevator bottom tics:" + bottomTics + "   Elevator top tics:" + topTics);
	}

	private void logPID() {
		logger.info("PID enabled:" + holdPID.isEnabled());
		logger.info("PID error:" + holdPID.getSetpoint());
		logger.info("PID error:" + holdPID.getError());
	}
	
}
