package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

public class PassLine extends Mission {
	private enum MissionStates {
		waiting, deploying, driving, done
	}

	private MissionStates curState = MissionStates.waiting;

//	private AutoDrive driveStep;
	private AutoDrive driveDegrees;

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			intake.autoDeploy();
			driveDegrees = new TurnDegrees(90);
//			driveStep = new DriveWithEncoder(96);// DriveWithEncoder(159.5);
			curState = MissionStates.deploying;
			break;
		case deploying:
			if (intake.isIntakeDown()) {
				curState = MissionStates.driving;
				elevator.setPosition(RobotMap.Elevator.EXCHANGE_HEIGHT);
			}
		case driving:
			driveDegrees.run();
			if (driveDegrees.isFinished()) {
				curState = MissionStates.done;
			}
			break;
		case done:
			driveDegrees = null;
			break;
		}
	}
}