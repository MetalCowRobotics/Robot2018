package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.DriveToWall;

public class AutoMission1 extends Mission {
	private enum MissionStates {
		waiting, driving, arrived, deploying, deployed, ejecting, ejected, done
	}

	private MissionStates curState = MissionStates.waiting;

	private DriveToWall driveStep;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			driveStep = new DriveToWall(6);
			// intake.deploy();
			// elevator.moveToSetPosition(SetPositions.switchWall);
			curState = MissionStates.driving;
			break;
		case driving:
			driveStep.run();
			if (driveStep.isFinished())
				curState = MissionStates.arrived;
			break;
		case arrived:
			curState = MissionStates.deploying;
			break;
		case deploying:
			// if (SetPositions.switchWall == elevator.getCurrentSetPostion()) {
			curState = MissionStates.deployed;
			// }
			break;
		case deployed:
			if (onMySide()) {
				// intake.autoEjectPowerCube();
				curState = MissionStates.ejecting;
			} else {
				curState = MissionStates.done;
			}
			break;
		case ejecting:
			// if (AutoCubeStates.ejected == intake.autoCubeState()) {
			curState = MissionStates.ejected;
			// }
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
