package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Climber {
	private static final Climber instance = new Climber();
	private static final Logger logger = Logger.getLogger(Climber.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();

	private static final SpeedController CLIMBER_MOTOR = new Talon(RobotMap.Climber.CLIMBER_MOTOR_CHANNEL);
	private static final Servo CLIMBER_SERVO = new Servo(RobotMap.Climber.CLIMBER_HELPER_SERVO_CHANNEL);

	private static final DigitalInput SERVO_SWITCH = new DigitalInput(RobotMap.Climber.LIMIT_SWITCH);
	
	// TODO: Motors
	boolean TouchingGear = true;
	int servoEngagedAngle = 180;
	int servoDisengagedAngle = 130;
	double servoAngle;
	ClimberState climberState = ClimberState.inactive;

	// TODO: Sensors

	private enum ClimberState {
		active, // saftey off, will take commands
		inactive, // safety on, no commands accepted
		preparing, feedingRope, // pushing up on stick, feeding out rope
		climbing // pulling in rope; ratchet locking
	}

	private Climber() {
		// Singleton
	}

	public static Climber getinstance() {
		return instance;
	}

	public void execute() {
		//System.out.println("Climber entry");
		//System.out.println("SERVO ANGLE: "+CLIMBER_SERVO.getAngle()+ "Actual raw? " +CLIMBER_SERVO.get()+ "    CLIMB THROTTLE: "+controller.getClimbThrottle());
		servoAngle = CLIMBER_SERVO.getAngle();

		if (controller.isClimberActivated()) {

			if (controller.getClimbThrottle() > 0.1) {// push up to feed rope

				if (SERVO_SWITCH.get()) {
					climberState = ClimberState.preparing;
					CLIMBER_MOTOR.set(-.30);
					CLIMBER_SERVO.setAngle(servoDisengagedAngle);
					System.out.println(SERVO_SWITCH.get()+" ############### Preparing to climb: "+CLIMBER_SERVO.getAngle());
				} else {
						climberState = ClimberState.feedingRope;
						System.out.println(" ############### Climbing: "+CLIMBER_SERVO.getAngle());
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
			System.out.println("climber activated");
			break;
		case inactive:
			System.out.println("I am INACTIVE");
			break;
		case preparing:
			System.out.println("we are Preparing to climb");
			break;
		case feedingRope:
			System.out.println("Feeding out the rope");
			break;
		case climbing:
			System.out.println("I am CLIMBING");
			break;
		default:
			System.out.println("Im broken ah ah ah ah ah ah ah ah ah ah ah !!!!!!!! tim, come fix me");

		}

	}

}
