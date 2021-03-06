package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

public class RightSideToLeftSwitch extends Mission {
	private enum MissionStates {
		waiting, driving1, driving2, arrived1, arrived2, turning1, turning2, reaching, reached, deploying, ejecting, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep1;
	private AutoDrive driveDegrees1;
	private AutoDrive driveStep2;
	private AutoDrive driveDegrees2;
	private AutoDrive driveToWall;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting:
			driveStep1 = new DriveWithEncoder(12);
			driveDegrees1 = new TurnDegrees(-90);
			driveStep2 = new DriveWithEncoder(84);
			driveDegrees2 = new TurnDegrees(90);
			//driveToWall = new DriveToWall(13);
			driveToWall = new DriveWithEncoder(12);
			intake.autoDeploy();
			curState = MissionStates.driving1;
			break;
		case driving1:
			driveStep1.run();
			if (driveStep1.isFinished())
				curState = MissionStates.arrived1;
			break;
		case arrived1:
			curState = MissionStates.turning1;
			break;
		case turning1:
			driveDegrees1.run();
			if (driveDegrees1.isFinished())
				curState = MissionStates.driving2;
			break;
		case driving2:
			driveStep2.run();
			if (driveStep2.isFinished())
				curState = MissionStates.turning2;
			break;
		case arrived2:
			curState = MissionStates.turning2;
			elevator.setPosition(RobotMap.Elevator.SWITCHWALL_HEIGHT);
			break;
		case turning2:
			driveDegrees2.run();
			if (driveDegrees2.isFinished())
				curState = MissionStates.reaching;
			break;
		case deploying:
			if (elevator.isAtHeight(RobotMap.Elevator.SWITCHWALL_HEIGHT)) {
				curState = MissionStates.reaching;
			}
			break;
		case reaching:
			driveToWall.run();
			if (driveToWall.isFinished())
				curState = MissionStates.reached;
			break;
		case reached:
			intake.autoEject();
			curState = MissionStates.ejecting;
			break;
		case ejecting:
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.done;
			}
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		default:
			break;
		}
	}
}