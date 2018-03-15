package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Climber {
	private static final Logger logger = Logger.getLogger(Climber.class.getName());
	private static final SpeedController CLIMBER_MOTOR = new Talon(RobotMap.Climber.CLIMBER_MOTOR_CHANNEL);
	private static final Servo CLIMBER_SERVO = new Servo(RobotMap.Climber.CLIMBER_HELPER_SERVO_CHANNEL);
 	private static final DigitalInput SERVO_SWITCH = new DigitalInput(RobotMap.Climber.LIMIT_SWITCH);
	private static final MasterControls controller = MasterControls.getInstance();
 	private static final Climber instance = new Climber();

	private enum ClimberState {
			active, // saftey off, will take commands
			inactive, // safety on, no commands accepted
			preparing, 
			feedingRope, // pushing up on stick, feeding out rope
			climbing // pulling in rope; ratchet locking
	}
	
	int servoEngagedAngle = 180;
	int servoDisengagedAngle = 130;
	ClimberState climberState = ClimberState.inactive;
	
	private Climber() {
		// Singleton
		logger.setLevel(RobotMap.LogLevels.climberClass);
	}

	public static Climber getinstance() {
		return instance;
	}

	public void execute() {
		if (controller.isClimberActivated()) {
			if (controller.getClimbThrottle() > 0.1) {// push up to feed rope
				if (SERVO_SWITCH.get()) {
					climberState = ClimberState.preparing;
					CLIMBER_MOTOR.set(-.30);
					CLIMBER_SERVO.setAngle(servoDisengagedAngle);
					logger.info(SERVO_SWITCH.get() + " ############### Preparing to climb: " + CLIMBER_SERVO.getAngle());
				} else {
					climberState = ClimberState.feedingRope;
					logger.info(" ############### Climbing: " + CLIMBER_SERVO.getAngle());
					CLIMBER_MOTOR.set(controller.getClimbThrottle());
				}
			} else if (controller.getClimbThrottle() < -0.1) {
				climberState = ClimberState.climbing;
				CLIMBER_MOTOR.set(controller.getClimbThrottle());
				CLIMBER_SERVO.setAngle(servoEngagedAngle);
			} else {
				climberState = ClimberState.active;
				CLIMBER_SERVO.setAngle(servoEngagedAngle);
				CLIMBER_MOTOR.stopMotor();
			}
		} else { // climber safteybutton not pressed
			climberState = ClimberState.inactive;
			CLIMBER_MOTOR.stopMotor();
			CLIMBER_SERVO.set(servoEngagedAngle);
		}
		switch (climberState) {
		case active:
			logger.info("climber activated");
			break;
		case inactive:
			logger.info("I am INACTIVE");
			break;
		case preparing:
			logger.info("we are Preparing to climb");
			break;
		case feedingRope:
			logger.info("Feeding out the rope");
			break;
		case climbing:
			logger.info("I am CLIMBING");
			break;
		default:
			logger.info("Im broken ah ah ah ah ah ah ah ah ah ah ah !!!!!!!! tim, come fix me");
		}
	}

}