package org.usfirst.frc.team4213.robot;

import java.util.logging.Level;

public class RobotMap {

	public final class DriverController {
		public static final int USB_PORT = 0;
	}

	public final class OperatorController {
		public static final int USB_PORT = 1;
	}

	public static final class Drivetrain {
		public static final double SPRINT_SPEED = 1;
		public static final double NORMAL_SPEED = 0.7;
		public static final double CRAWL_SPEED = 0.5;
		public static final int[] LEFT_MOTORS = { 3, 4 };
		public static final int[] RIGHT_MOTORS = { 1, 2 };
		public static final int LEFT_MOTOR_CHANNEL1 = 3;// CAN
		public static final int LEFT_MOTOR_CHANNEL2 = 4;// CAN
		public static final int RIGHT_MOTOR_CHANNEL1 = 1;// CAN
		public static final int RIGHT_MOTOR_CHANNEL2 = 2;// CAN
		public static final double MY_GYRO_CHANNEL = 1;
		public static final int RANGE_FINDER = 1;// Analog Input/Output
		public static final int WHEEL_DIAMETER = 6;
		public static final int LEFT_ENCODER_1 = 2; // DIO
		public static final int LEFT_ENCODER_2 = 3; // DIO
		public static final int RIGHT_ENCODER_1 = 0; // DIO
		public static final int RIGHT_ENCODER_2 = 1; // DIO
		public static final boolean MCR_DRIVE_MODE = true;
	}

	public final class DriveToWall {
		public static final double TOP_SPEED = .7;
		public static final int SLOW_DOWN_DISTANCE = 18;
		public static final double BOTTOM_SPEED = .4;
		public static final double MAX_ADJUSTMENT = .4;
	}

	public final class DriveWithEncoder {
		public static final double TOP_SPEED = .9;
		public static final double BOTTOM_SPEED = .5;
		public static final double MAX_ADJUSTMENT = .4;
		public static final int TICS_PER_ROTATION = 354; // need to try 360
		public static final double INCHES_PER_ROTATION = Math.PI * RobotMap.Drivetrain.WHEEL_DIAMETER;
		public static final double SLOW_DOWN_DISTANCE = (12 / INCHES_PER_ROTATION) * TICS_PER_ROTATION;
	}

	public final class TurnDegrees {
		public static final double TOP_SPEED = 0;
		public static final double MAX_ADJUSTMENT = .5;
		public static final double VARIANCE = 3; // .25
		public static final double SLOW_ADJUSTMENT = .2;
		public static final double SLOW_VARIANCE = 5;
	}

	public final class DriveStraightTime {
		public static final double TOP_SPEED = .6;
		public static final double MAX_ADJUSTMENT = .4;
		public static final double BOTTOM_SPEED = .3;
		public static final double SLOW_DOWN_TIME = 2;
	}

	public final class Intake {
		public static final double INTAKE_SPEED = -0.7;
		public static final double EJECT_SPEED = 0.7;
		public static final int LEFT_MOTOR_CHANNEL = 0;// PWM
		public static final int RIGHT_MOTOR_CHANNEL = 1;// PWM
		public static final double RAISE_INTAKE_SPEED = -.4;
		public static final double LOWER_INTAKE_SPEED = .4;
		public static final int ANGLE_MOTOR_CHANNEL = 7; // CAN
		// public static final int RANGE_FINDER = 0; // Analog Input/Output
		public static final double AUTO_EJECT_SECONDS = 2.0;
		public static final int BOX_SENSOR = 9; // DIO
	}

	public final class Elevator {
		public static final double UP_SPEED = 0.2;// motor is reversed
		public static final double DOWN_SPEED = -0.8;// motor is reversed
		public static final int ELEVATOR_CHANNEL1 = 5;// CAN
		public static final int ELEVATOR_CHANNEL2 = 6;// CAN
		public static final int LIMIT_SWITCH_TOP = 6; // DIO -- VERIFIED via Multimeter
		public static final int LIMIT_SWITCH_BOTTOM = 7; // DIO -- Two Stages are wired in series... as one switch.
		public static final int ELEVATOR_ENCODER_1 = 4; // DIO
		public static final int ELEVATOR_ENCODER_2 = 5; // DIO
		public static final double EXCHANGE_HEIGHT = 1.75;
		public static final double SWITCHWALL_HEIGHT = 20;
		public static final double SCALE_MID_HEIGHT = 80;
		public static final double ELEVATOR_WINCH_DIAMETER = 19 / 8;
		public static final int TICS_PER_ROTATION = 354; // need to try 360
		public static final double INCHES_PER_ROTATION = Math.PI * RobotMap.Elevator.ELEVATOR_WINCH_DIAMETER;
		public static final double SLOW_DOWN_DISTANCE = (8 / INCHES_PER_ROTATION) * TICS_PER_ROTATION;
	}

	public final class Climber {
		public static final int CLIMBER_MOTOR_CHANNEL1 = 2; // PWM - using Y-cable only needs 1 port
	}

	public static final class LogLevels {
		public static final Level robotClass = Level.WARNING;
		public static final Level autoDriveClass = Level.WARNING;
		public static final Level climberClass = Level.WARNING;
		public static final Level driveStraightTimeClass = Level.WARNING;
		public static final Level driveToWallClass = Level.WARNING;
		public static final Level driveTrainClass = Level.WARNING;
		public static final Level driveWithEncoderClass = Level.WARNING;
		public static final Level elevatorClass = Level.WARNING;
		public static final Level intakeClass = Level.WARNING;
		public static final Level turnDegreesClass = Level.WARNING;
		public static final Level masterControlsClass = Level.WARNING;
		public static final Level hamburgerDashboardClass = Level.WARNING;
		public static final Level componentBuilderClass = Level.WARNING;
		public static final Level missionClass = Level.WARNING;
	}

}
