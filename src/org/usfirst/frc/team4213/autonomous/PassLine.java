package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.RobotMap;

public class PassLine extends Mission {
	private enum MissionStates {
		waiting, driving, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			intake.deploy();
			elevator.moveElevatorToPosition(RobotMap.Elevator.EXCHANGE_HEIGHT);
			driveStep = new DriveWithEncoder(159.5);
			curState = MissionStates.driving;
			break;
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