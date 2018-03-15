package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Climber {
	private static final Climber instance = new Climber();
	private static final Logger logger = Logger.getLogger(Climber.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();

	private static final SpeedController CLIMBER_MOTOR = new Talon(RobotMap.Climber.CLIMBER_MOTOR_CHANNEL1);

	private Climber() {
		// Singleton
	}

	public static Climber getinstance() {
		return instance;
	}

	public void execute() {
		if (controller.climbEnabled()) {
			CLIMBER_MOTOR.set(controller.getClimbThrottle());
		} else {
			CLIMBER_MOTOR.stopMotor();
		}
	}

}
