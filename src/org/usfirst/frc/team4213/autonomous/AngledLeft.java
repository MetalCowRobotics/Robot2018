package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;
import org.usfirst.frc.team4213.robot.RobotMap;

public class AngledLeft extends Mission {
	private enum MissionStates {
		waiting, driving, arrived, turning, reaching, reached, deploying, ejecting, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;
	private AutoDrive driveDegrees;
	private AutoDrive driveToWall;

	public void execute() {
		switch (curState) {
		case waiting:
			driveStep = new DriveWithEncoder(80);
			driveDegrees = new TurnDegrees(45);
			driveToWall = new DriveToWall(13);
			intake.autoDeploy();
			curState = MissionStates.driving;
			break;
		case driving:
			driveStep.run();
			if (driveStep.isFinished())
				curState = MissionStates.turning;
			break;
		case turning:
			driveDegrees.run();
			if (driveDegrees.isFinished())
				curState = MissionStates.deploying;
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
			//intake.execute();
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