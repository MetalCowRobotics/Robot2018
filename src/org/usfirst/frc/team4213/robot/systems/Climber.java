package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Climber {
	private static final Climber instance = new Climber();
	private static final Logger logger = Logger.getLogger(Climber.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();

	private static final SpeedController CLIMBER_MOTOR = new MCR_SRX(RobotMap.Climber.CLIMBER_MOTOR_CHANNEL1);
	private static final SpeedController CLIMBER_HELPER_SERVO = new MCR_SRX(RobotMap.Climber.CLIMBER_HELPER_SERVO_CHANNEL);

	// TODO: Motors

	Servo ClimberHelperServo = new Servo(2);
	boolean TouchingGear = true;

	// TODO: Sensors

	private Climber() {
		// Singleton
	}

	public static Climber getinstance() {
		return instance;
	}

	public void execute() {
		System.out.println("Climber entry");
		if (controller.climbEnabled()) {
			CLIMBER_MOTOR.set(controller.getClimbThrottle());
			System.out.println(controller.getClimbThrottle());
			//ClimberHelperServo.setAngle(90);
		} else {
			CLIMBER_MOTOR.stopMotor();
		}
		
		if (controller.isClimberSafetyOn()) {
			ClimberHelperServo.setAngle(180);
		}
		else {
			ClimberHelperServo.setAngle(0);
		}
	}

}
