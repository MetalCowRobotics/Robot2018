package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDInterface;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import java.util.logging.Logger;
public class Elevator {
	private static final Elevator instance = new Elevator();
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();
	
	private static SpeedController ELEVATOR_MOTOR = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1);
//	private static SpeedControllerGroup ELEVATOR_MOTOR = new SpeedControllerGroup (new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1));
//	//, new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL2));

	private static final Encoder elevatorEncoder = new Encoder(RobotMap.Elevator.ELEVATOR_ENCODER_1, RobotMap.Elevator.ELEVATOR_ENCODER_2, false, EncodingType.k4X);
	//private static final Encoder otherEncoder = new Encoder(2, 3, false, CounterBase.EncodingType.k4X);

	DigitalInput topLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	DigitalInput bottomLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);

	double kP = .2;
	double kI = 0;
	double kD = 0;
	PIDController holdPID; 
	boolean firstTime = true;
	
	MotorState motorState = MotorState.OFF; // start state is off
	ElevatorState elevatorState = ElevatorState.BOTTOM;
	boolean AutoPosition = false;
	double encoderTarget;
	double currentPosition;
	double bottomTics;
	double topTics;
	double safteyZone = (12 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
	double safteyTopSpeed = .5;


	
	private Elevator() {
		// Singleton Pattern
	}

	public void execute() {
		logger.info("================== iteration ==============================");
		logger.info("Speed:" + ELEVATOR_MOTOR.get() );
		if (firstTime) {
			//ELEVATOR_MOTOR.setInverted(true);
			startEncoder();
			topTics = bottomTics + (72 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
			logger.info("bottomTics:" + bottomTics);
			logger.info("topTics:" + topTics);
			firstTime = false;
		}
		logger.info("PID error:"+holdPID.getError());
		logger.info("PID enables:"+holdPID.isEnabled());
		logger.info("Elevator Up: "+this.isElevatorAtTop()+" Elevator Down: "+this.isElevatorAtBottom());
		logger.info("elevator encoder tics:" + getEncoderTics());
		logger.info("elevator target:" + encoderTarget);
		logger.info("AutoPosition:" + AutoPosition);		
		if (AutoPosition) {
			logger.info("elevator boolean:" + (getEncoderTics() > encoderTarget));
			if (getEncoderTics() > encoderTarget) {
				stop();
				AutoPosition = false;
			}
		} else {
			double elevatorSpeed = controller.getElevatorThrottle();
			logger.info("elevator throttle:" + controller.getElevatorThrottle());
			if (isElevatorAtTop() && elevatorSpeed > 0) {
				stop();
			} else if (isElevatorAtBottom() && elevatorSpeed < 0) {
				stop();
			} else {
				moveElevator(elevatorSpeed);
			}
			// get current encoder reading
			// currentPosition = this.getEncoderTics();
		}

	}

	private void startEncoder() {
		if (null != holdPID)
			holdPID.free();
		holdPID = new PIDController(kP, kI, kD, elevatorEncoder, ELEVATOR_MOTOR, 20);
		holdPID.setOutputRange(-.4, .6);
		holdPID.setAbsoluteTolerance(5);
		bottomTics = getEncoderTics();
	}

	public static Elevator getInstance() {
		return instance;
	}

	private void moveElevator(double elevatorSpeed) {
		if (getEncoderTics() > (topTics - safteyZone) && elevatorSpeed > 0) {
			disablePID();
			ELEVATOR_MOTOR.set(Math.min(elevatorSpeed, safteyTopSpeed));
			motorState = MotorState.ON;
		} else if (getEncoderTics() < (bottomTics + safteyZone) && elevatorSpeed < 0) {
			disablePID();
			ELEVATOR_MOTOR.set(Math.min(elevatorSpeed, safteyTopSpeed));
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
	
	public void moveUp() {
		if (!movingUp()) {
			setElevatorSpeed(RobotMap.Elevator.UP_SPEED);
		}
	}

	public void moveDown() {
		if (!movingDown()) {
			setElevatorSpeed(RobotMap.Elevator.DOWN_SPEED);
		}
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
		if (!isElevatorAtBottom()) {
			motorState = MotorState.HOLD;
			startEncoder();
			holdPID.setSetpoint(getEncoderTics());
			holdPID.enable();
		}
	}

	public void moveElevatorToPosition(double height) {
		logger.info("In moveToPosition");
		holdPID.disable();
		AutoPosition = true;
		encoderTarget = getEncoderTics() + ((height / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION);
		setElevatorSpeed(RobotMap.Elevator.UP_SPEED);
		logger.info("leaving moveToPosition:" + ELEVATOR_MOTOR.get() );
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
}
