package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;
import org.usfirst.frc.team4213.robot.RobotMap;

public class RightSideToLeftSwitch extends Mission {
	private enum MissionStates {
		waiting, left, driving1, driving2, arrived1, larrived2, lturning1, lturning2, lturned1, lturned2, lreaching, lreached, ldeploying, ldeployed, lejecting, lejected, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep1;
	private AutoDrive driveStep;
	private AutoDrive driveDegrees1;
	private AutoDrive driveStep2;
	private AutoDrive driveDegrees2;
	private AutoDrive driveToWall;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting:
			driveStep1 = new DriveWithEncoder(60);
			driveStep = new DriveWithEncoder(12);
			driveDegrees1 = new TurnDegrees(-90);
			driveStep2 = new DriveWithEncoder(84);
			driveDegrees2 = new TurnDegrees(90);
			driveToWall = new DriveToWall(13);
			intake.autoDeploy();
			curState = MissionStates.driving1;
			break;
		case driving1:
			driveStep1.run();
			if (driveStep1.isFinished())
				curState = MissionStates.arrived1;
			break;
		case arrived1:
			curState = MissionStates.lturning1;
			break;
		case lturning1:
			driveDegrees1.run();
			if (driveDegrees1.isFinished())
				curState = MissionStates.lturned1;
			break;
		case lturned1:
			curState = MissionStates.driving2;
			break;
		case driving2:
			driveStep2.run();
			if (driveStep2.isFinished())
				curState = MissionStates.larrived2;
			break;
		case larrived2:
			curState = MissionStates.lturning2;
			elevator.setPosition(RobotMap.Elevator.SWITCHWALL_HEIGHT);
			break;
		case lturning2:
			driveDegrees2.run();
			if (driveDegrees2.isFinished())
				curState = MissionStates.lturned2;
			break;
		case lturned2:
			curState = MissionStates.ldeploying;
			break;
		case ldeploying:
			if (elevator.isAtHeight(RobotMap.Elevator.SWITCHWALL_HEIGHT)) {
				curState = MissionStates.ldeployed;
			}
			break;
		case ldeployed:
			curState = MissionStates.lreaching;
			break;
		case lreaching:
			driveToWall.run();
			if (driveToWall.isFinished())
				curState = MissionStates.lreached;
			break;
		case lreached:
			intake.autoEject();
			curState = MissionStates.lejecting;
			break;
		case lejecting:
			//intake.execute();
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.lejected;
			}
			break;
		case lejected:
			// could do a secondary mission
			curState = MissionStates.done;
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		default:
			break;
		}
	}
}