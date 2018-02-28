package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.lib14.UtilityMethods;
import org.usfirst.frc.team4213.robot.HamburgerDashboard;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class Elevator {
	private static final Elevator instance = new Elevator();
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();

	private static SpeedController ELEVATOR_MOTOR = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1);
	// private static SpeedControllerGroup ELEVATOR_MOTOR = new SpeedControllerGroup
	// (new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1));
	// //, new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL2));

	private static final Encoder elevatorEncoder = new Encoder(RobotMap.Elevator.ELEVATOR_ENCODER_1,
			RobotMap.Elevator.ELEVATOR_ENCODER_2, false, EncodingType.k4X);
	// private static final Encoder otherEncoder = new Encoder(2, 3, false,
	// CounterBase.EncodingType.k4X);

	private HamburgerDashboard dash = HamburgerDashboard.getInstance();

	DigitalInput topLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	DigitalInput bottomLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);

	// double kP = .2;
	// double kI = 0;
	// double kD = 0;
	PDController holdPID;
	boolean firstTime = true;

	MotorState motorState = MotorState.OFF; // start state is off
	ElevatorState elevatorState = ElevatorState.BOTTOM;
	boolean AutoPosition = false;
	double encoderTarget;
	double currentPosition;
	double bottomTics;
	double topTics;
	double safetyZone = (12 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
	double safteyTopSpeed = .5;
	private boolean holding = false;
	private double range = 7000;
	private double tolerance = .01;
	private double maxAdjustment = .5;
	private double lastPosition;
	private PDController loop = new PDController(0, .5, 0);

	private Elevator() {
		// Singleton Pattern
	}

	public void execute() {
		logger.info("================== iteration ==============================");
		logger.info("Speed:" + ELEVATOR_MOTOR.get());
		dash.pushPID(holdPID);
		if (firstTime) {
			// ELEVATOR_MOTOR.setInverted(true);
//			startEncoder();
			topTics = bottomTics + (72 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
			logger.info("bottomTics:" + bottomTics);
			logger.info("topTics:" + topTics);
			firstTime = false;
		}
		// logger.info("PID error:" + holdPID.getError());
		// logger.info("PID enables:" + holdPID.isEnabled());
		logger.info("Elevator Up: " + this.isElevatorAtTop() + " Elevator Down: " + this.isElevatorAtBottom());
		logger.info("elevator encoder tics:" + getEncoderTics());
		logger.info("elevator target:" + encoderTarget);
		logger.info("AutoPosition:" + AutoPosition);
//		if (AutoPosition) {
//			logger.info("elevator boolean:" + (getEncoderTics() > encoderTarget));
//			if (getEncoderTics() > encoderTarget) {
//				stop();
//				AutoPosition = false;
//			}
//		} else {
//			double elevatorSpeed = controller.getElevatorThrottle();
//			logger.info("elevator throttle:" + controller.getElevatorThrottle());
//			if (MotorState.HOLD == motorState && 0 == elevatorSpeed) {
//
//			} else {
//				disablePID();
//				motorState = MotorState.ON;
//				if (isElevatorAtTop() && elevatorSpeed > 0) {
//					stop();
//				} else if (isElevatorAtBottom() && elevatorSpeed < 0) {
//					stop();
//				} else {
//					if (0 == elevatorSpeed) {
//						stop();
//					} else {
//						moveElevator(elevatorSpeed);
//					}
//				}
//			}
//		}

		if (0 == controller.getElevatorThrottle()) {
			setElevatorSpeed(.03 + loop.calculateAdjustment(getEncoderTics()));
			HamburgerDashboard.getInstance().pushPID(loop);
		} else {
			setElevatorSpeed(controller.getElevatorThrottle());
			loop.setSetPoint(getEncoderTics());
			loop.reset();
		}

	}

	public void setPositionTics(double tics) {
		loop.setSetPoint(tics);
		loop.reset();
	}
	
	private void adjustHoldPid() {
		double currentError = Math.abs(getEncoderTics()) > tolerance ? getEncoderTics() : 0;
		double correction = holdPID.calculateAdjustment(currentError / range);
		setElevatorSpeed(maxCorrection(correction));
	}

	private double maxCorrection(double correction) {
		return Math.abs(correction) > maxAdjustment ? correction : UtilityMethods.copySign(correction, maxAdjustment);
	}

	public void setElevatorSpeed(double speed) {
		if (isMovingUp(speed) && isElevatorAtTop()) {
			stop();
			//hold(topTics - 20);
		} else if (isMovingDown(speed) && isElevatorAtBottom()) {
			stop();
		} else {
			ELEVATOR_MOTOR.set(maxSpeed(speed));
		}
	}

	public void hold(double currentPosition) {
		holdPID = new PDController(currentPosition / range, dash.getKP(), dash.getKD());
		holding = true;
	}

	private boolean isMovingUp(double speed) {

		return speed > 0;
	}

	private boolean isMovingDown(double speed) {
		return speed < 0;
	}

	private double maxSpeed(double speed) {
		if (isMovingUp(speed) && inUpperSafetyZone()) {
			return Math.min(speed, RobotMap.Elevator.SafeSpeed);
		} else if (isMovingDown(speed) && inLowerSafetyZone()) {
			return Math.max(speed, -RobotMap.Elevator.SafeSpeed);
		} else {
			return speed;
		}
	}

	private boolean inLowerSafetyZone() {
		return getEncoderTics() > (topTics - safetyZone);
	}

	private boolean inUpperSafetyZone() {
		return getEncoderTics() < (bottomTics + safetyZone);
	}

	private void startEncoder() {
		// if (null != holdPID)
		// holdPID.free();
		// holdPID = new PIDController(dash.getKP(), dash.getKI(), dash.getKD(),
		// elevatorEncoder, ELEVATOR_MOTOR, 20);
		// holdPID.setOutputRange(dash.getOutputMin(), dash.getOutputMax());
		// holdPID.setAbsoluteTolerance(dash.getTolerance());
	}

	public static Elevator getInstance() {
		return instance;
	}

	private void moveElevator(double elevatorSpeed) {
		if (getEncoderTics() > (topTics - safetyZone) && elevatorSpeed > 0) {
			ELEVATOR_MOTOR.set(Math.min(elevatorSpeed, safteyTopSpeed));
		} else if (getEncoderTics() < (bottomTics + safetyZone) && elevatorSpeed < 0) {
			ELEVATOR_MOTOR.set(Math.min(elevatorSpeed, safteyTopSpeed));
		} else {
			ELEVATOR_MOTOR.set(elevatorSpeed);
		}
	}

	private void disablePID() {
		// if (null != holdPID)
		// holdPID.disable();
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

	// private void setElevatorSpeed(double speed) {
	// motorState = (speed < 0) ? MotorState.DOWN : MotorState.UP;
	// ELEVATOR_MOTOR.set(speed);
	// }
	public void stop() {
		ELEVATOR_MOTOR.stopMotor();
		// if (!isElevatorAtBottom()) {
		// motorState = MotorState.HOLD;
		// startEncoder();
		// holdPID.setSetpoint(getEncoderTics());
		// holdPID.enable();
		// } else {
		// ELEVATOR_MOTOR.stopMotor();
		// motorState = MotorState.OFF;
		// }
	}

	public void moveElevatorToPosition(double height) {
		logger.info("In moveToPosition");
		// holdPID.disable();
		AutoPosition = true;
		encoderTarget = getEncoderTics()
				+ ((height / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION);
		setElevatorSpeed(RobotMap.Elevator.UP_SPEED);
		logger.info("leaving moveToPosition:" + ELEVATOR_MOTOR.get());
	}

	private double getEncoderTics() {
		return elevatorEncoder.getDistance();
	}

	private boolean isElevatorAtTop() {
		return !topLimit.get(); // For some reason this is inverted in the hardware, correcting here in software
	}

	private boolean isElevatorAtBottom() {
		return !bottomLimit.get(); // for some reason this is inverted in hardware, correcting here in software
	}

	private enum MotorState {
		ON, OFF, UP, DOWN, HOLD
	}

	private enum ElevatorState {
		TOP, MIDDLE, BOTTOM
	}
}
