package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;

import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team4213.robot.RobotMap;

public class PassLine extends Mission {
	private enum MissionStates {
		waiting, deploying, driving, done
	}

	private Timer timer = new Timer();
	
	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			timer.reset();
			timer.start();
			intake.deploy();
			driveStep = new DriveWithEncoder(36);//DriveWithEncoder(159.5);
			curState = MissionStates.deploying;
			break;
		case deploying:
			if (timer.get() > 3) {
				//cancel deploy
				curState = MissionStates.driving;
				elevator.moveElevatorToPosition(RobotMap.Elevator.EXCHANGE_HEIGHT);
			}
		case driving:
			driveStep.run();
			if (driveStep.isFinished()) {
				curState = MissionStates.done;
			}
			break;
		case done:
			driveStep = null;
			break;
		}
	}
}