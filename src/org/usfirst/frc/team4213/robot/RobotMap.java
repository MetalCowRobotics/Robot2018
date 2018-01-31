package org.usfirst.frc.team4213.robot;

public class RobotMap {

	public final class DriverController {
		public static final int USB_PORT = 0;
	}

	public final class OperatorController {
		public static final int USB_PORT = 0;
	}

	public final class Drivetrain {

		public static final int LEFT_MOTOR_CHANNEL = 0;
		public static final int RIGHT_MOTOR_CHANNEL = 1;
		public static final double SPRINT_SPEED = 0.7;
		public static final double NORMAL_SPEED = 0.5;
		public static final double CRAWL_SPEED = 0.3;
		// public static final double MY_GYRO_CHANNEL = 1;

	}

	public final class Intake {
		public static final double INTAKE_SPEED = 0.2;
		public static final double EJECT_SPEED = -0.2;
		public static final int LEFT_MOTOR_CHANNEL = 2;
		public static final int RIGHT_MOTOR_CHANNEL = 3;
		public static final int LIMIT_SWITCH_CHANNEL = 9;
		public static final int RANGE_FINDER = 0;
	}

	public final class Elevator {
		public static final int ELEVATOR_CHANNEL = 4;
		public static final double UP_SPEED = 0.2;
		public static final double DOWN_SPEED = -0.2;
	}
}
