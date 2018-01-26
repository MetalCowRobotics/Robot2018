package org.usfirst.frc.team4213.robot;

public class RobotMap {

	public final class DriverController {
		public static final int USB_PORT = 0;
	}

	public final class OperatorController {
		public static final int USB_PORT = 1;
	}

	public final class Drivetrain {
		public static final int LEFT_MOTOR_CHANNEL = 0;
		public static final int RIGHT_MOTOR_CHANNEL = 1;
		public static final double SPRINT_SPEED = 1;
		public static final double NORMAL_SPEED = 0.7;
		public static final double CRAWL_SPEED = 0.5;
	}

	public final class Intake {
		public static final double INTAKE_SPEED = 0.5;
		public static final double EJECT_SPEED = -0.5;
		public static final int LEFT_MOTOR_CHANNEL = 5;
		public static final int LIMIT_SWITCH_CHANNEL = 0;
	}

}
