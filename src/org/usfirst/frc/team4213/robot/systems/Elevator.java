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
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());
	private static final Elevator instance = new Elevator();
	private static final HamburgerDashboard dash = HamburgerDashboard.getInstance();
	private static final MasterControls controller = MasterControls.getInstance();

	private static SpeedController ELEVATOR_MOTOR = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1);
	// private static SpeedControllerGroup ELEVATOR_MOTOR = new SpeedControllerGroup
	// (new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1));
	// //, new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL2));
	private static final Encoder elevatorEncoder = new Encoder(RobotMap.Elevator.ELEVATOR_ENCODER_1,
			RobotMap.Elevator.ELEVATOR_ENCODER_2, false, EncodingType.k4X);
	// private static final Encoder otherEncoder = new Encoder(2, 3, false,
	// CounterBase.EncodingType.k4X);
	DigitalInput topLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	DigitalInput bottomLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);

	// double kP = .2;
	// double kI = 0;
	// double kD = 0;
	boolean firstTime = true;
	double currentPosition;
	double bottomTics;
	double topTics;
	private double lastPosition;
	private PDController holdPID = new PDController(0, .5, 0);

	private Elevator() {
		// Singleton Pattern
		logger.setLevel(RobotMap.LogLevels.elevatorClass);
	}

	public static Elevator getInstance() {
		return instance;
	}

	public void execute() {
		logger.info("================== elevator iteration ==============================");
		logger.info("Elevator Up: " + this.isElevatorAtTop() + " Elevator Down: " + this.isElevatorAtBottom());
		logger.info("elevator encoder tics:" + getEncoderTics());
		if (firstTime) {
			// ELEVATOR_MOTOR.setInverted(true);
			topTics = bottomTics + inchesToTics(72);
			logger.info("bottomTics:" + bottomTics);
			logger.info("topTics:" + topTics);
			firstTime = false;
		}
		if (0 == controller.getElevatorThrottle()) {
			setElevatorSpeed(holdPID.calculateAdjustment(getEncoderTics()));
			HamburgerDashboard.getInstance().pushPID(holdPID);
		} else {
			setElevatorSpeed(controller.getElevatorThrottle());
			setPositionTics(getEncoderTics());
		}
	}

	public void setPositionTics(double tics) {
		holdPID.setSetPoint(tics);
		holdPID.reset();
	}

	public void setPosition(double inches) {
		logger.info("================== elevator set position ==============================");
		setPositionTics(inchesToTics(inches));
		logger.info("Target Tics:" + inchesToTics(inches));
	}

	private double inchesToTics(double inches) {
		return (inches / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
	}

	public void setElevatorSpeed(double speed) {
		if (isMovingUp(speed) && isElevatorAtTop()) {
			stop();
		} else if (isMovingDown(speed) && isElevatorAtBottom()) {
			stop();
		} else {
			ELEVATOR_MOTOR.set(maxSpeed(speed));
		}
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
		return getEncoderTics() > (topTics - RobotMap.Elevator.SafeZone);
	}

	private boolean inUpperSafetyZone() {
		return getEncoderTics() < (bottomTics + RobotMap.Elevator.SafeZone);
	}

	public void stop() {
		ELEVATOR_MOTOR.stopMotor();
	}

	private double getEncoderTics() {
		return elevatorEncoder.getDistance();
	}

	public boolean isAtHeight(double heightInches) {
		return UtilityMethods.between(inchesToTics(heightInches), getEncoderTics() - 10, getEncoderTics() + 10);
	}

	private boolean isElevatorAtTop() {
		return !topLimit.get(); // For some reason this is inverted in the hardware, correcting here in software
	}

	private boolean isElevatorAtBottom() {
		return !bottomLimit.get(); // for some reason this is inverted in hardware, correcting here in software
	}
}