package org.usfirst.frc.team4213.robot.systems;

import java.util.logging.Logger;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;

public class Climber {
	private static final Climber instance = new Climber();
	private static final Logger logger = Logger.getLogger(Climber.class.getName());

	private static final MasterControls controller = MasterControls.getInstance();

	private static final SpeedController CLIMBER_MOTOR1 = new Talon(RobotMap.Climber.CLIMBER_MOTOR_CHANNEL1);
	private static final SpeedController CLIMBER_MOTOR2 = new Talon(RobotMap.Climber.CLIMBER_MOTOR_CHANNEL2);
	private static SpeedControllerGroup ClimberSpeedControllerGroup = new SpeedControllerGroup (CLIMBER_MOTOR1, CLIMBER_MOTOR2);


	// TODO: Motors

	// TODO: Sensors

	private Climber() {
		// Singleton
	}

	public static Climber getinstance() {
		return instance;
	}

	public void execute() {
		ClimberSpeedControllerGroup.set(controller.getClimbThrottle());
	}

}
