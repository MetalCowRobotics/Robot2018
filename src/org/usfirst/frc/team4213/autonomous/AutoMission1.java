package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

public class AutoMission1 extends Mission {
	private enum MissionStates {
		waiting, driving, arrived, turning, turned, reaching, reached, deploying, deployed, ejecting, ejected, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;
	private AutoDrive driveDegrees;
	private AutoDrive driveToWall;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			driveStep = new DriveWithEncoder(159.5);
			driveDegrees = new TurnDegrees(90);
			driveToWall = new DriveToWall(2);
			intake.deploy();
			// elevator.moveToSetPosition(SetPositions.switchWall);
			System.out.println("waiting");
			curState = MissionStates.driving;
			break;
		case driving:
			driveStep.run();
			if (driveStep.isFinished())
				System.out.println("driving");
			curState = MissionStates.arrived;
			break;
		case arrived:
			System.out.println("arrived");
			curState = MissionStates.turning;
			break;
		case turning:
			driveDegrees.run();
			if (driveDegrees.isFinished())
				System.out.println("turning");
			curState = MissionStates.turned;
			break;
		case turned:
			curState = MissionStates.deploying;
			break;
		case deploying:
			// if (SetPositions.switchWall == elevator.getCurrentSetPostion()) {
			System.out.println("deploying");
			curState = MissionStates.deployed;
			// }
			break;
		case deployed:
			curState = MissionStates.reaching;
			break;
		case reaching:
			driveToWall.run();
			if (driveToWall.isFinished())
				curState = MissionStates.reached;
			break;
		case reached:
			if (onMySide()) {
				System.out.println("deployed");
				intake.autoEject();
				curState = MissionStates.ejecting;
			} else {
				curState = MissionStates.done;
			}
			break;
		case ejecting:
			System.out.println("checking eject time");
			intake.execute();
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.ejected;
			}
			break;
		case ejected:
			// could do a secondary mission
			curState = MissionStates.done;
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		}
	}

}
