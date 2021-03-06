package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;

public class PassLine extends Mission {
	private enum MissionStates {
		waiting, deploying, driving, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			intake.autoDeploy();
			driveStep = new DriveWithEncoder(130);
			curState = MissionStates.driving;
			break;
		case deploying:
			if (intake.isIntakeDown()) {
				curState = MissionStates.driving;
				elevator.setPosition(RobotMap.Elevator.EXCHANGE_HEIGHT);
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